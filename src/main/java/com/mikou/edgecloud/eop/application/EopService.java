package com.mikou.edgecloud.eop.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.common.spi.NatsClient;
import com.mikou.edgecloud.edge.api.dto.EdgeItemDto;
import com.mikou.edgecloud.edge.api.dto.NicIpDto;
import com.mikou.edgecloud.edge.domain.enums.EdgeStatus;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicIpEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicIpMapper;
import com.mikou.edgecloud.eop.api.dto.*;
import com.mikou.edgecloud.eop.domain.enums.EopDirection;
import com.mikou.edgecloud.eop.domain.enums.EopOccupierType;
import com.mikou.edgecloud.eop.domain.enums.EopServiceStatus;
import com.mikou.edgecloud.eop.domain.events.EopNotifyMessage;
import com.mikou.edgecloud.eop.domain.events.EopServiceExpiredEvent;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.*;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopAppMapper;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopBoundMapper;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopProductMapper;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopServiceMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikou.edgecloud.eop.domain.model.EopBoundParams;
import com.mikou.edgecloud.eop.domain.model.EopServiceEntitlements;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EopService {

    private final EopBoundMapper eopBoundMapper;
    private final EopPortService eopPortService;
    private final EopServiceMapper eopServiceMapper;
    private final EopProductMapper eopProductMapper;
    private final EopAppMapper eopAppMapper;
    private final EdgeNicIpMapper edgeNicIpMapper;
    private final EdgeMapper edgeMapper;
    private final ObjectMapper objectMapper;
    private final NatsClient natsClient;

    public EopService(EopBoundMapper eopBoundMapper,
                      EopPortService eopPortService,
                      EopServiceMapper eopServiceMapper,
                      EopProductMapper eopProductMapper,
                      EopAppMapper eopAppMapper,
                      EdgeNicIpMapper edgeNicIpMapper,
                      EdgeMapper edgeMapper,
                      NatsClient natsClient,
                      ObjectMapper objectMapper) {
        this.eopBoundMapper = eopBoundMapper;
        this.eopPortService = eopPortService;
        this.eopServiceMapper = eopServiceMapper;
        this.eopProductMapper = eopProductMapper;
        this.eopAppMapper = eopAppMapper;
        this.edgeNicIpMapper = edgeNicIpMapper;
        this.edgeMapper = edgeMapper;
        this.natsClient = natsClient;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public UUID purchaseProduct(UUID ownerId, UUID productTag, Integer durationMonths) {
        EopProductEntity product = eopProductMapper.selectOne(new LambdaQueryWrapper<EopProductEntity>()
                .eq(EopProductEntity::getTag, productTag));
        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + productTag);
        }

        Instant now = Instant.now();
        // 计算到期时间
        Instant expiredAt = now.plus(durationMonths * 30L, java.time.temporal.ChronoUnit.DAYS);

        EopServiceEntitlements entitlements = new EopServiceEntitlements();
        entitlements.setMaxOutboundCount(product.getMaxOutboundCount());
        entitlements.setMaxInboundCount(product.getMaxInboundCount());
        entitlements.setOutboundMaxConnections(product.getOutboundMaxConnections());
        entitlements.setInboundMaxConnections(product.getInboundMaxConnections());
        entitlements.setMaxOriginTargets(product.getMaxOriginTargets());

        EopServiceEntity service = new EopServiceEntity()
                .setTag(UUID.randomUUID())
                .setOwnerId(ownerId)
                .setProductId(product.getId())
                .setMonthlyPrice(product.getMonthlyPrice())
                .setEntitlements(entitlements)
                .setStatus(EopServiceStatus.INACTIVE) // 初始状态
                .setExpiredAt(expiredAt)
                .setCreatedAt(now)
                .setUpdatedAt(now);

        eopServiceMapper.insert(service);
        return service.getTag();
    }

    @Transactional
    public UUID createOutbound(UUID ownerId, UUID serviceTag, UUID eopTag, Integer addrId, OutboundParamsDto paramsDto) {
        EopServiceEntity service = getServiceByTag(serviceTag.toString());
        EopAppEntity app = eopAppMapper.selectOne(new LambdaQueryWrapper<EopAppEntity>()
                .eq(EopAppEntity::getEopTag, eopTag));
        if (app == null) {
            throw new IllegalArgumentException("EOP not found: " + eopTag);
        }

        // 1. 幂等检查
        Long count = eopBoundMapper.selectCount(new LambdaQueryWrapper<EopBoundEntity>()
                .eq(EopBoundEntity::getServiceId, service.getId())
                .eq(EopBoundEntity::getDirection, EopDirection.OUTBOUND));
        if (count > 0) {
            throw new IllegalStateException("Outbound already exists for service: " + serviceTag);
        }

        // 2. 配额校验 (从 Service Entitlements 获取)
        Integer maxConnections = service.getEntitlements() != null ? service.getEntitlements().getOutboundMaxConnections() : 128;

        // 3. 转换并校验 Outbound 规则
        EopBoundParams params = new EopBoundParams()
                .setHost(paramsDto.getHost())
                .setParameters(paramsDto.getParameters());

        if (params.getHost() == null || params.getHost().isEmpty()) {
            throw new IllegalArgumentException("Outbound host cannot be empty");
        }

        Instant now = Instant.now();
        EopBoundEntity entity = new EopBoundEntity()
                .setTag(UUID.randomUUID())
                .setServiceId(service.getId())
                .setEopId(app.getId())
                .setOwnerId(ownerId)
                .setProductId(service.getProductId())
                .setDirection(EopDirection.OUTBOUND)
                .setAddrId(addrId)
                .setMaxConnections(maxConnections)
                .setExtraParams(params)
                .setCreatedAt(now)
                .setUpdatedAt(now);

        eopBoundMapper.insert(entity);
        
        // 通知 NATS
        notifyNats(EopNotifyMessage.ACTION_CREATE_BOUND, getEdgeTag(app.getEdgeId()), entity);

        return entity.getTag();
    }

    private void notifyNats(String action, UUID edgeTag, Object data) {
        String subject = "eop.notify";
        natsClient.publish(subject, new EopNotifyMessage(action, edgeTag, data));
    }

    private UUID getEdgeTag(Integer edgeId) {
        if (edgeId == null) return null;
        EdgeEntity edge = edgeMapper.selectById(edgeId);
        return edge != null ? edge.getEdgeTag() : null;
    }

    @Transactional
    public UUID createInbound(UUID ownerId, UUID serviceTag, UUID eopTag, Integer addrId, InboundParamsDto paramsDto) {
        EopServiceEntity service = getServiceByTag(serviceTag.toString());
        EopAppEntity app = eopAppMapper.selectOne(new LambdaQueryWrapper<EopAppEntity>()
                .eq(EopAppEntity::getEopTag, eopTag));
        if (app == null) {
            throw new IllegalArgumentException("EOP not found: " + eopTag);
        }

        EopServiceEntitlements entitlements = service.getEntitlements();

        // 1. 数量校验
        Long currentInboundCount = eopBoundMapper.selectCount(new LambdaQueryWrapper<EopBoundEntity>()
                .eq(EopBoundEntity::getServiceId, service.getId())
                .eq(EopBoundEntity::getDirection, EopDirection.INBOUND));
        Integer maxInboundCount = entitlements != null ? entitlements.getMaxInboundCount() : 0;
        if (currentInboundCount >= maxInboundCount) {
            throw new IllegalStateException("Inbound count exceeds service limit: " + maxInboundCount);
        }

        // 2. 连接数
        Integer maxConnections = entitlements != null ? entitlements.getInboundMaxConnections() : 128;

        // 3. 转换并校验 Inbound 规则
        EopBoundParams params = new EopBoundParams()
                .setPort(paramsDto.getPort())
                .setOutTag(paramsDto.getOutTag())
                .setTransferRoute(paramsDto.getTransferRoute())
                .setParameters(paramsDto.getParameters());

        if (params.getPort() == null || params.getOutTag() == null || 
            params.getTransferRoute() == null || params.getTransferRoute().isEmpty()) {
            throw new IllegalArgumentException("Inbound: port, outTag, and transferRoute cannot be empty");
        }

        // 4. 端口校验
        if (!eopPortService.isPortAvailable(addrId, params.getPort())) {
            throw new IllegalStateException(String.format("Port %d is already occupied", params.getPort()));
        }

        Instant now = Instant.now();
        EopBoundEntity entity = new EopBoundEntity()
                .setTag(UUID.randomUUID())
                .setServiceId(service.getId())
                .setEopId(app.getId())
                .setOwnerId(ownerId)
                .setProductId(service.getProductId())
                .setDirection(EopDirection.INBOUND)
                .setAddrId(addrId)
                .setMaxConnections(maxConnections)
                .setExtraParams(params)
                .setCreatedAt(now)
                .setUpdatedAt(now);

        eopBoundMapper.insert(entity);
        
        // 5. 执行端口占用 (Inbound 独有) - 使用 insert 后的实体 ID 作为 occupierId
        if (params.getPort() != null) {
             eopPortService.executeOccupyPort(addrId, params.getPort(), EopOccupierType.EOP_BOUND, entity.getId());
        }

        // 通知 NATS
        notifyNats(EopNotifyMessage.ACTION_CREATE_BOUND, getEdgeTag(app.getEdgeId()), entity);

        return entity.getTag();
    }

    @Transactional
    public void deleteBound(Integer id) {
        EopBoundEntity bound = eopBoundMapper.selectById(id);
        if (bound == null) {
            return;
        }

        EopAppEntity app = eopAppMapper.selectById(bound.getEopId());

        // 释放端口占用 (通过领域事件发起申请)
        eopPortService.requestReleaseAllPortsByOccupier(EopOccupierType.EOP_BOUND, bound.getId());

        // 物理删除
        eopBoundMapper.deleteById(id);

        // 通知 NATS
        if (app != null) {
            notifyNats(EopNotifyMessage.ACTION_DELETE_BOUND, getEdgeTag(app.getEdgeId()), bound);
        }
    }

    @Transactional
    public void deleteBoundByTag(String tag) {
        EopBoundEntity bound = eopBoundMapper.selectOne(new LambdaQueryWrapper<EopBoundEntity>()
                .eq(EopBoundEntity::getTag, tag));
        if (bound != null) {
            deleteBound(bound.getId());
        }
    }

    /**
     * 级联回收：当服务到期时释放所有 Bound
     */
    @EventListener
    @Transactional
    public void onServiceExpired(EopServiceExpiredEvent event) {
        List<EopBoundEntity> bounds = eopBoundMapper.selectList(new LambdaQueryWrapper<EopBoundEntity>()
                .eq(EopBoundEntity::getServiceId, event.serviceId()));
        
        for (EopBoundEntity bound : bounds) {
            deleteBound(bound.getId());
        }
    }

    @Transactional
    public void renewService(String tag, int months) {
        EopServiceEntity service = getServiceByTag(tag);
        Instant currentExpiry = service.getExpiredAt();
        if (currentExpiry == null) {
            currentExpiry = Instant.now();
        }
        
        // 简单增加月份（这里为了演示使用 Instant，实际建议使用 ZonedDateTime 方便计算月份）
        Instant newExpiry = currentExpiry.plus(months * 30L, java.time.temporal.ChronoUnit.DAYS);
        service.setExpiredAt(newExpiry);
        service.setUpdatedAt(Instant.now());
        eopServiceMapper.updateById(service);
    }

    private EopServiceEntity getServiceByTag(String tag) {
        EopServiceEntity service = eopServiceMapper.selectOne(new LambdaQueryWrapper<EopServiceEntity>()
                .eq(EopServiceEntity::getTag, tag));
        if (service == null) {
            throw new IllegalArgumentException("Service not found: " + tag);
        }
        return service;
    }

    public Page<EopServiceDto> listMyServices(UUID ownerId, Pageable pageable) {
        if (ownerId == null) return new Page<>();

        Page<EopServiceEntity> pageParam = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        Page<EopServiceEntity> result = eopServiceMapper.selectPage(pageParam, new LambdaQueryWrapper<EopServiceEntity>()
                .eq(EopServiceEntity::getOwnerId, ownerId)
                .isNull(EopServiceEntity::getRemovedAt));

        if (result.getRecords().isEmpty()) return new Page<>(result.getCurrent(), result.getSize(), result.getTotal());

        List<Integer> productIds = result.getRecords().stream().map(EopServiceEntity::getProductId).distinct().collect(Collectors.toList());
        Map<Integer, EopProductEntity> productMap = eopProductMapper.selectBatchIds(productIds)
                .stream().collect(Collectors.toMap(EopProductEntity::getId, p -> p));

        Page<EopServiceDto> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream().map(s -> {
            EopProductEntity p = productMap.get(s.getProductId());
            return new EopServiceDto()
                    .setTag(s.getTag())
                    .setProductName(p != null ? p.getName() : "Unknown")
                    .setProductTag(p != null ? p.getTag() : null)
                    .setMonthlyPrice(s.getMonthlyPrice())
                    .setStatus(s.getStatus())
                    .setEntitlements(s.getEntitlements())
                    .setExpiredAt(s.getExpiredAt())
                    .setCreatedAt(s.getCreatedAt());
        }).collect(Collectors.toList()));
        
        return dtoPage;
    }

    public Page<EopBoundDto> listBounds(UUID ownerId, UUID eopTag, UUID serviceTag, Pageable pageable) {
        LambdaQueryWrapper<EopBoundEntity> wrapper = new LambdaQueryWrapper<EopBoundEntity>()
                .eq(EopBoundEntity::getOwnerId, ownerId)
                .isNull(EopBoundEntity::getRemovedAt);

        if (eopTag != null) {
            EopAppEntity app = eopAppMapper.selectOne(new LambdaQueryWrapper<EopAppEntity>()
                    .eq(EopAppEntity::getEopTag, eopTag));
            if (app != null) {
                wrapper.eq(EopBoundEntity::getEopId, app.getId());
            } else {
                return new Page<>();
            }
        }

        if (serviceTag != null) {
            EopServiceEntity service = eopServiceMapper.selectOne(new LambdaQueryWrapper<EopServiceEntity>()
                    .eq(EopServiceEntity::getTag, serviceTag));
            if (service != null) {
                wrapper.eq(EopBoundEntity::getServiceId, service.getId());
            } else {
                return new Page<>();
            }
        }

        Page<EopBoundEntity> pageParam = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        Page<EopBoundEntity> result = eopBoundMapper.selectPage(pageParam, wrapper);

        Page<EopBoundDto> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream().map(this::mapToBoundDto).collect(Collectors.toList()));
        return dtoPage;
    }

    private EopBoundDto mapToBoundDto(EopBoundEntity entity) {
        return new EopBoundDto()
                .setTag(entity.getTag())
                .setDirection(entity.getDirection())
                .setMaxConnections(entity.getMaxConnections())
                .setExtraParams(entity.getExtraParams())
                .setCreatedAt(entity.getCreatedAt());
    }

    private NicIpDto mapToIpDto(EdgeNicIpEntity entity) {
        return new NicIpDto()
                .setId(entity.getId())
                .setPrivateIp(entity.getPrivateIp())
                .setPublicIp(entity.getPublicIp())
                .setStatus(entity.getStatus())
                .setAllocated(entity.getAllocatedToId() != null)
                .setAllocatedToType(entity.getAllocatedToType())
                .setAllocatedToId(entity.getAllocatedToId());
    }

    private EopAppDto mapToAppDto(EopAppEntity entity) {
        EopAppDto dto = new EopAppDto()
                .setId(entity.getId())
                .setEopTag(entity.getEopTag())
                .setSettings(entity.getSettings())
                .setCreatedAt(entity.getCreatedAt())
                .setUpdatedAt(entity.getUpdatedAt());

        EdgeEntity edge = edgeMapper.selectById(entity.getEdgeId());
        if (edge != null) {
            dto.setEdgeTag(edge.getEdgeTag());
            EdgeItemDto edgeDto = new EdgeItemDto()
                    .setId(edge.getId())
                    .setEdgeTag(edge.getEdgeTag())
                    .setName(edge.getName())
                    .setRegionId(edge.getRegionId())
                    .setStatus(edge.getStatus())
                    .setOnline(edge.getStatus() == EdgeStatus.ENABLED);
            dto.setEdge(edgeDto);
        }
        return dto;
    }

    private EopProductEntity getProductById(Integer id) {
        EopProductEntity product = eopProductMapper.selectById(id);
        if (product == null) {
            throw new IllegalStateException("Product not found for service");
        }
        return product;
    }
}
