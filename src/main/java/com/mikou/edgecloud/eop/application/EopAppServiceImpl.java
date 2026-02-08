package com.mikou.edgecloud.eop.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.common.enums.FeatureType;
import com.mikou.edgecloud.edge.api.dto.EdgeItemDto;
import com.mikou.edgecloud.edge.api.dto.EdgeListQuery;
import com.mikou.edgecloud.edge.api.dto.NicIpDto;
import com.mikou.edgecloud.edge.domain.enums.EdgeStatus;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicIpEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicIpMapper;
import com.mikou.edgecloud.eop.api.dto.EopAppDto;
import com.mikou.edgecloud.eop.api.dto.EopBoundDto;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopAppEntity;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopBoundEntity;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopAppMapper;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopBoundMapper;
import com.mikou.edgecloud.eop.domain.model.EopSettings;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EopAppServiceImpl implements EopAppService {

    private final EopAppMapper eopAppMapper;
    private final EopBoundMapper eopBoundMapper;
    private final EdgeMapper edgeMapper;
    private final EdgeNicIpMapper edgeNicIpMapper;
    private final EopPortService eopPortService;
    private final EopProtocolService eopProtocolService;

    public EopAppServiceImpl(EopAppMapper eopAppMapper, EopBoundMapper eopBoundMapper,
                             EdgeMapper edgeMapper, EdgeNicIpMapper edgeNicIpMapper,
                             EopPortService eopPortService, EopProtocolService eopProtocolService) {
        this.eopAppMapper = eopAppMapper;
        this.eopBoundMapper = eopBoundMapper;
        this.edgeMapper = edgeMapper;
        this.edgeNicIpMapper = edgeNicIpMapper;
        this.eopPortService = eopPortService;
        this.eopProtocolService = eopProtocolService;
    }

    @Override
    public Page<EopAppDto> listApps(EdgeListQuery query, Pageable pageable) {
        Page<EopAppEntity> pageParam;
        if (pageable != null) {
            pageParam = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        } else {
            pageParam = new Page<>(1, 20);
        }

        Page<EopAppEntity> result = eopAppMapper.selectPageWithRegion(
                pageParam, query.getCityId(), query.getRegionId(), query.getCountryId());

        Page<EopAppDto> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream()
                .map(this::mapToAppDto)
                .collect(Collectors.toList()));

        return dtoPage;
    }

    @Override
    @Transactional
    public EopAppDto updateSettings(UUID eopTag, EopSettings settings) {
        eopProtocolService.updateSettings(eopTag, settings);
        EopAppEntity app = eopAppMapper.selectOne(new LambdaQueryWrapper<EopAppEntity>()
                .eq(EopAppEntity::getEopTag, eopTag));
        return mapToAppDto(app);
    }

    @Override
    public void createProtocol(UUID eopTag, String protocol, EopSettings.ProtocolListener listener) {
        eopProtocolService.createProtocol(eopTag, protocol, listener);
    }

    @Override
    public void destroyProtocol(UUID eopTag, String protocol, Integer port) {
        eopProtocolService.destroyProtocol(eopTag, protocol, port);
    }

    @Override
    public Page<EopBoundDto> listBoundsByEopTag(UUID eopTag, Pageable pageable) {
        EopAppEntity app = eopAppMapper.selectOne(new LambdaQueryWrapper<EopAppEntity>()
                .eq(EopAppEntity::getEopTag, eopTag));
        if (app == null) {
            throw new IllegalArgumentException("EOP not found: " + eopTag);
        }

        Page<EopBoundEntity> pageParam = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        Page<EopBoundEntity> result = eopBoundMapper.selectPage(pageParam, new LambdaQueryWrapper<EopBoundEntity>()
                .eq(EopBoundEntity::getEopId, app.getId()));
        
        Page<EopBoundDto> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream()
                .map(this::mapToBoundDto)
                .collect(Collectors.toList()));
        return dtoPage;
    }

    @Override
    @Transactional
    public void syncApps() {
        List<EdgeEntity> allEdges = edgeMapper.selectList(null);
        Instant now = Instant.now();
        for (EdgeEntity edge : allEdges) {
            if (edge.getFeatures() != null && edge.getFeatures().isEnabled(FeatureType.EOP)) {
                // 检查是否已有 eop_app
                Long count = eopAppMapper.selectCount(new LambdaQueryWrapper<EopAppEntity>()
                        .eq(EopAppEntity::getEdgeId, edge.getId()));
                if (count == null || count == 0) {
                    EopAppEntity app = new EopAppEntity()
                            .setEopTag(UUID.randomUUID())
                            .setEdgeId(edge.getId())
                            .setCreatedAt(now)
                            .setUpdatedAt(now);
                    eopAppMapper.insert(app);
                }
            }
        }
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
}
