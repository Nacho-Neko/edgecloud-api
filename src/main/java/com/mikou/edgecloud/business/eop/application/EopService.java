package com.mikou.edgecloud.business.eop.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.business.eop.api.dto.*;
import com.mikou.edgecloud.business.eop.domain.enums.EopStatus;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.entity.EopAppEntity;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.entity.EopBoundEntity;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.entity.EopProductEntity;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.entity.EopServiceEntity;
import com.mikou.edgecloud.common.spi.NatsClient;
import com.mikou.edgecloud.common.spi.PortAvailabilityChecker;
import com.mikou.edgecloud.business.eop.domain.events.EopPortOccupyRequestedEvent;
import com.mikou.edgecloud.business.eop.domain.events.EopPortReleaseRequestedEvent;
import com.mikou.edgecloud.edge.api.dto.EdgeItemDto;
import com.mikou.edgecloud.edge.api.dto.NicIpDto;
import com.mikou.edgecloud.edge.domain.enums.EdgeStatus;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicIpEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicIpMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeAreaMapper;
import com.mikou.edgecloud.business.eop.domain.enums.EopDirection;
import com.mikou.edgecloud.business.eop.domain.events.EopNotifyMessage;
import com.mikou.edgecloud.business.eop.domain.events.EopServiceExpiredEvent;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.mapper.EopAppMapper;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.mapper.EopBoundMapper;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.mapper.EopProductMapper;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.mapper.EopServiceMapper;
import org.springframework.context.ApplicationEventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikou.edgecloud.business.eop.domain.model.EopBoundParams;
import com.mikou.edgecloud.business.eop.domain.model.EopServiceEntitlements;
import com.mikou.edgecloud.business.domain.ProductStatus;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EopService {

    private final EopBoundMapper eopBoundMapper;
    private final PortAvailabilityChecker portAvailabilityChecker;
    private final ApplicationEventPublisher eventPublisher;
    private final EopServiceMapper eopServiceMapper;
    private final EopProductMapper eopProductMapper;
    private final EopAppMapper eopAppMapper;
    private final EdgeNicIpMapper edgeNicIpMapper;
    private final EdgeMapper edgeMapper;
    private final EdgeAreaMapper regionMapper;
    private final ObjectMapper objectMapper;
    private final NatsClient natsClient;
    private final com.mikou.edgecloud.account.infrastructure.persistence.mapper.AccountMapper accountMapper;

    public EopService(EopBoundMapper eopBoundMapper,
                      PortAvailabilityChecker portAvailabilityChecker,
                      ApplicationEventPublisher eventPublisher,
                      EopServiceMapper eopServiceMapper,
                      EopProductMapper eopProductMapper,
                      EopAppMapper eopAppMapper,
                      EdgeNicIpMapper edgeNicIpMapper,
                      EdgeMapper edgeMapper,
                      EdgeAreaMapper regionMapper,
                      NatsClient natsClient,
                      ObjectMapper objectMapper,
                      com.mikou.edgecloud.account.infrastructure.persistence.mapper.AccountMapper accountMapper) {
        this.eopBoundMapper = eopBoundMapper;
        this.portAvailabilityChecker = portAvailabilityChecker;
        this.eventPublisher = eventPublisher;
        this.eopServiceMapper = eopServiceMapper;
        this.eopProductMapper = eopProductMapper;
        this.eopAppMapper = eopAppMapper;
        this.edgeNicIpMapper = edgeNicIpMapper;
        this.edgeMapper = edgeMapper;
        this.regionMapper = regionMapper;
        this.natsClient = natsClient;
        this.objectMapper = objectMapper;
        this.accountMapper = accountMapper;
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
                .setStatus(ProductStatus.ACTIVE) // 初始状态为激活
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
        if (!portAvailabilityChecker.isPortAvailable(addrId, params.getPort())) {
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

        // 5. 发布端口占用事件（Edge 层监听并执行）
        if (params.getPort() != null) {
            eventPublisher.publishEvent(
                    new EopPortOccupyRequestedEvent(addrId, params.getPort(), "EOP_BOUND", entity.getId()));
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

        // 发布端口释放事件（Edge 层监听并执行）
        eventPublisher.publishEvent(new EopPortReleaseRequestedEvent("EOP_BOUND", bound.getId()));

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
     * 级联回收：当服务到期时暂停所有 Bound
     */
    @EventListener
    @Transactional
    public void onServiceExpired(EopServiceExpiredEvent event) {
        suspendServiceBounds(event.serviceId());
    }

    /**
     * 暂停服务下的所有 Bound
     */
    @Transactional
    public void suspendServiceBounds(Integer serviceId) {
        List<EopBoundEntity> bounds = eopBoundMapper.selectList(new LambdaQueryWrapper<EopBoundEntity>()
                .eq(EopBoundEntity::getServiceId, serviceId)
                .isNull(EopBoundEntity::getRemovedAt));

        Instant now = Instant.now();
        for (EopBoundEntity bound : bounds) {
            if (bound.getStatus() == EopStatus.RUNNING) {
                bound.setStatus(EopStatus.PAUSED);
                bound.setUpdatedAt(now);
                eopBoundMapper.updateById(bound);

                // 通知 NATS 停止
                EopAppEntity app = eopAppMapper.selectById(bound.getEopId());
                if (app != null) {
                    notifyNats(EopNotifyMessage.ACTION_STOP_BOUND, getEdgeTag(app.getEdgeId()), bound);
                }
            }
        }
    }

    @Transactional
    public void resumeServiceBounds(Integer serviceId) {
        List<EopBoundEntity> bounds = eopBoundMapper.selectList(new LambdaQueryWrapper<EopBoundEntity>()
                .eq(EopBoundEntity::getServiceId, serviceId)
                .isNull(EopBoundEntity::getRemovedAt));

        Instant now = Instant.now();
        for (EopBoundEntity bound : bounds) {
            if (bound.getStatus() == EopStatus.PAUSED) {
                bound.setStatus(EopStatus.RUNNING);
                bound.setUpdatedAt(now);
                eopBoundMapper.updateById(bound);

                // 通知 NATS 恢复 (重新创建指令)
                EopAppEntity app = eopAppMapper.selectById(bound.getEopId());
                if (app != null) {
                    notifyNats(EopNotifyMessage.ACTION_CREATE_BOUND, getEdgeTag(app.getEdgeId()), bound);
                }
            }
        }
    }

    @Transactional
    public void renewService(String tag, int months) {
        EopServiceEntity service = getServiceByTag(tag);
        
        // 如果服务是暂停状态，续费后可能需要恢复
        boolean wasSuspended = service.getStatus() == ProductStatus.SUSPENDED;
        
        Instant currentExpiry = service.getExpiredAt();
        if (currentExpiry == null) {
            currentExpiry = Instant.now();
        }
        
        // 简单增加月份（这里为了演示使用 Instant，实际建议使用 ZonedDateTime 方便计算月份）
        Instant newExpiry = currentExpiry.plus(months * 30L, java.time.temporal.ChronoUnit.DAYS);
        service.setExpiredAt(newExpiry);
        service.setUpdatedAt(Instant.now());
        
        if (wasSuspended && newExpiry.isAfter(Instant.now())) {
            service.setStatus(ProductStatus.ACTIVE);
            resumeServiceBounds(service.getId());
        }
        
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
                    .setServiceTag(s.getTag())
                    .setProductName(p != null ? p.getName() : "Unknown")
                    .setProductTag(p != null ? p.getTag() : null)
                    .setMonthlyPrice(s.getMonthlyPrice())
                    .setProductStatus(s.getStatus())
                    .setEntitlements(s.getEntitlements())
                    .setExpiredAt(s.getExpiredAt())
                    .setCreatedAt(s.getCreatedAt());
        }).collect(Collectors.toList()));
        
        return dtoPage;
    }

    /**
     * 查询服务列表（支持筛选条件）- 供管理后台使用
     */
    public Page<EopServiceDto> listServicesWithFilter(UUID ownerId, ProductStatus status, Pageable pageable) {
        Page<EopServiceEntity> pageParam = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        
        LambdaQueryWrapper<EopServiceEntity> wrapper = new LambdaQueryWrapper<EopServiceEntity>()
                .isNull(EopServiceEntity::getRemovedAt);
        
        // 可选的 ownerId 筛选
        if (ownerId != null) {
            wrapper.eq(EopServiceEntity::getOwnerId, ownerId);
        }
        
        // 可选的 status 筛选
        if (status != null) {
            wrapper.eq(EopServiceEntity::getStatus, status);
        }
        
        Page<EopServiceEntity> result = eopServiceMapper.selectPage(pageParam, wrapper);

        if (result.getRecords().isEmpty()) return new Page<>(result.getCurrent(), result.getSize(), result.getTotal());

        List<Integer> productIds = result.getRecords().stream().map(EopServiceEntity::getProductId).distinct().collect(Collectors.toList());
        Map<Integer, EopProductEntity> productMap = eopProductMapper.selectBatchIds(productIds)
                .stream().collect(Collectors.toMap(EopProductEntity::getId, p -> p));

        // 关联账户信息
        Map<UUID, com.mikou.edgecloud.business.api.dto.AccountSimpleDto> accountMap = buildAccountMap(ownerId, result.getRecords());

        Page<EopServiceDto> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream().map(s -> {
            EopProductEntity p = productMap.get(s.getProductId());
            com.mikou.edgecloud.business.api.dto.AccountSimpleDto accountDto = accountMap.get(s.getOwnerId());
            return new EopServiceDto()
                    .setServiceTag(s.getTag())
                    .setProductName(p != null ? p.getName() : "Unknown")
                    .setProductTag(p != null ? p.getTag() : null)
                    .setMonthlyPrice(s.getMonthlyPrice())
                    .setProductStatus(s.getStatus())
                    .setEntitlements(s.getEntitlements())
                    .setExpiredAt(s.getExpiredAt())
                    .setCreatedAt(s.getCreatedAt())
                    .setAccount(accountDto);
        }).collect(Collectors.toList()));
        
        return dtoPage;
    }

    /**
     * 构建账户信息映射
     * 如果指定了 ownerId，只查询一次；否则批量查询所有相关账户
     */
    private Map<UUID, com.mikou.edgecloud.business.api.dto.AccountSimpleDto> buildAccountMap(UUID ownerId, List<EopServiceEntity> services) {
        if (ownerId != null) {
            // 指定了 accountId，只查询一次
            com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountEntity account = 
                    accountMapper.selectById(ownerId);
            if (account != null) {
                com.mikou.edgecloud.business.api.dto.AccountSimpleDto dto = new com.mikou.edgecloud.business.api.dto.AccountSimpleDto()
                        .setAccountId(account.getId())
                        .setUsername(account.getUsername())
                        .setEmail(account.getEmail());
                return Map.of(ownerId, dto);
            }
            return Map.of();
        } else {
            // 没有指定 accountId，批量查询所有相关账户
            List<UUID> ownerIds = services.stream()
                    .map(EopServiceEntity::getOwnerId)
                    .distinct()
                    .collect(Collectors.toList());
            
            if (ownerIds.isEmpty()) {
                return Map.of();
            }
            
            List<com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountEntity> accounts = 
                    accountMapper.selectBatchIds(ownerIds);
            
            return accounts.stream().collect(Collectors.toMap(
                    com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountEntity::getId,
                    account -> new com.mikou.edgecloud.business.api.dto.AccountSimpleDto()
                            .setAccountId(account.getId())
                            .setUsername(account.getUsername())
                            .setEmail(account.getEmail())
            ));
        }
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
                .setStatus(entity.getStatus())
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
                    .setStatus(edge.getStatus())
                    .setOnline(edge.getStatus() == EdgeStatus.ENABLED)
                    .setFeatures(edge.getFeatures())
                    .setCreatedAt(edge.getCreatedAt())
                    .setUpdatedAt(edge.getUpdatedAt())
                    .setCpuCores(edge.getCpuCores())
                    .setCpuModel(edge.getCpuModel())
                    .setTotalMemory(edge.getTotalMemory())
                    .setOsType(edge.getOsType())
                    .setOsVersion(edge.getOsVersion())
                    .setOsArch(edge.getOsArch())
                    .setEdgeVersion(edge.getEdgeVersion());
            
            // 查询并设置区域信息
            if (edge.getRegionId() != null) {
                com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeAreaEntity regionEntity = 
                        regionMapper.selectById(edge.getRegionId());
                if (regionEntity != null) {
                    com.mikou.edgecloud.edge.api.dto.RegionTreeDto regionDto = 
                            new com.mikou.edgecloud.edge.api.dto.RegionTreeDto()
                            .setId(regionEntity.getId())
                            .setCode(regionEntity.getCode())
                            .setName(regionEntity.getName())
                            .setLevel(regionEntity.getLevel());
                    edgeDto.setRegion(regionDto);
                }
            }
            
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