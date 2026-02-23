package com.mikou.edgecloud.business.eop.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.common.enums.FeatureType;
import com.mikou.edgecloud.edge.api.dto.*;
import com.mikou.edgecloud.edge.domain.enums.EdgeStatus;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.*;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.*;
import com.mikou.edgecloud.business.eop.api.dto.EopAppDto;
import com.mikou.edgecloud.business.eop.api.dto.EopBoundDto;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.entity.EopAppEntity;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.entity.EopBoundEntity;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.mapper.EopAppMapper;
import com.mikou.edgecloud.business.eop.infrastructure.persistence.mapper.EopBoundMapper;
import com.mikou.edgecloud.business.eop.domain.model.EopSettings;
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
    private final EdgeAreaMapper regionMapper;

    public EopAppServiceImpl(EopAppMapper eopAppMapper, EopBoundMapper eopBoundMapper,
                             EdgeMapper edgeMapper,  EdgeAreaMapper regionMapper) {
        this.eopAppMapper = eopAppMapper;
        this.eopBoundMapper = eopBoundMapper;
        this.edgeMapper = edgeMapper;
        this.regionMapper = regionMapper;
    }

    @Override
    public Page<EopAppDto> listApps(EdgeListQuery query, Pageable pageable) {
        Page<EopAppEntity> pageParam = pageable != null
                ? new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize())
                : new Page<>(1, 20);

        Page<EopAppEntity> result = eopAppMapper.selectPageWithRegion(
                pageParam, query.getCityId(), query.getRegionId(), query.getCountryId());

        Page<EopAppDto> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream().map(this::mapToAppDto).collect(Collectors.toList()));
        return dtoPage;
    }

    @Override
    @Transactional
    public EopAppDto updateSettings(UUID eopTag, EopSettings settings) {
        EopAppEntity app = eopAppMapper.selectOne(new LambdaQueryWrapper<EopAppEntity>()
                .eq(EopAppEntity::getEopTag, eopTag));
        if (app == null) throw new IllegalArgumentException("EOP not found: " + eopTag);

        EopSettings current = app.getSettings() != null ? app.getSettings() : new EopSettings();
        current.setAllowedPortRange(settings.getAllowedPortRange());
        app.setSettings(current).setUpdatedAt(Instant.now());
        eopAppMapper.updateById(app);
        return mapToAppDto(app);
    }

    @Override
    public Page<EopBoundDto> listBoundsByEopTag(UUID eopTag, Pageable pageable) {
        EopAppEntity app = eopAppMapper.selectOne(new LambdaQueryWrapper<EopAppEntity>()
                .eq(EopAppEntity::getEopTag, eopTag));
        if (app == null) throw new IllegalArgumentException("EOP not found: " + eopTag);

        Page<EopBoundEntity> pageParam = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        Page<EopBoundEntity> result = eopBoundMapper.selectPage(pageParam,
                new LambdaQueryWrapper<EopBoundEntity>().eq(EopBoundEntity::getEopId, app.getId()));

        Page<EopBoundDto> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream().map(this::mapToBoundDto).collect(Collectors.toList()));
        return dtoPage;
    }

    @Override
    @Transactional
    public void syncApps() {
        List<EdgeEntity> allEdges = edgeMapper.selectList(null);
        Instant now = Instant.now();
        for (EdgeEntity edge : allEdges) {
            if (edge.getFeatures() != null && edge.getFeatures().isEnabled(FeatureType.EOP)) {
                Long count = eopAppMapper.selectCount(new LambdaQueryWrapper<EopAppEntity>()
                        .eq(EopAppEntity::getEdgeId, edge.getId()));
                if (count == null || count == 0) {
                    eopAppMapper.insert(new EopAppEntity()
                            .setEopTag(UUID.randomUUID())
                            .setEdgeId(edge.getId())
                            .setCreatedAt(now)
                            .setUpdatedAt(now));
                }
            }
        }
    }

    // ── helpers ──────────────────────────────────────────────────────────────

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
                    .setId(edge.getId()).setEdgeTag(edge.getEdgeTag()).setName(edge.getName())
                    .setStatus(edge.getStatus()).setOnline(edge.getStatus() == EdgeStatus.ENABLED)
                    .setFeatures(edge.getFeatures()).setCreatedAt(edge.getCreatedAt()).setUpdatedAt(edge.getUpdatedAt())
                    .setCpuCores(edge.getCpuCores()).setCpuModel(edge.getCpuModel())
                    .setTotalMemory(edge.getTotalMemory()).setOsType(edge.getOsType())
                    .setOsVersion(edge.getOsVersion()).setOsArch(edge.getOsArch())
                    .setEdgeVersion(edge.getEdgeVersion());
            if (edge.getRegionId() != null) {
                EdgeAreaEntity region = regionMapper.selectById(edge.getRegionId());
                if (region != null) {
                    edgeDto.setRegion(new RegionTreeDto()
                            .setId(region.getId()).setCode(region.getCode())
                            .setName(region.getName()).setLevel(region.getLevel()));
                }
            }
            dto.setEdge(edgeDto);
        }
        return dto;
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
}