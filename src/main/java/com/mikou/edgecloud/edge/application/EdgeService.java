
package com.mikou.edgecloud.edge.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.edge.api.dto.*;
import com.mikou.edgecloud.edge.domain.enums.EdgeStatus;
import com.mikou.edgecloud.edge.domain.model.EdgeFeatures;
import com.mikou.edgecloud.edge.domain.registry.EdgeRegistry;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicIpEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeAreaEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicIpMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeAreaMapper;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EdgeService {

    private final EdgeMapper edgeMapper;
    private final EdgeNicMapper nicMapper;
    private final EdgeNicIpMapper nicIpMapper;
    private final EdgeAreaMapper regionMapper;
    private final EdgeRegistry edgeRegistry;
    private final EdgeConverter edgeConverter;
    private final EdgeNicConverter nicConverter;

    public EdgeService(EdgeMapper edgeMapper, EdgeNicMapper nicMapper, EdgeNicIpMapper nicIpMapper,
                       EdgeAreaMapper regionMapper, EdgeRegistry edgeRegistry,
                       EdgeConverter edgeConverter, EdgeNicConverter nicConverter) {
        this.edgeMapper = edgeMapper;
        this.nicMapper = nicMapper;
        this.nicIpMapper = nicIpMapper;
        this.regionMapper = regionMapper;
        this.edgeRegistry = edgeRegistry;
        this.edgeConverter = edgeConverter;
        this.nicConverter = nicConverter;
    }

    @Transactional
    public Integer create(CreateEdgeRequest req) {
        if (req == null || req.getRegionId() == null) {
            throw new IllegalArgumentException("Missing regionId");
        }

        EdgeAreaEntity region = regionMapper.selectById(req.getRegionId());
        if (region == null) {
            throw new IllegalArgumentException("Region not found: " + req.getRegionId());
        }

        UUID edgeTag = UUID.randomUUID();

        Instant now = Instant.now();
        EdgeEntity entity = new EdgeEntity()
                .setEdgeTag(edgeTag)
                .setName(req.getName())
                .setRegionId(req.getRegionId())
                .setStatus(EdgeStatus.ENABLED)
                .setCreatedAt(now)
                .setUpdatedAt(now);

        edgeMapper.insert(entity);
        return entity.getId();
    }

    private String generateSequentialGuid() {
        UUID uuid = UUID.randomUUID();
        long timestamp = System.currentTimeMillis();

        return String.format("%08x-%04x-%04x-%04x-%012x",
                timestamp & 0xFFFFFFFFL,
                (timestamp >> 32) & 0xFFFFL,
                uuid.getMostSignificantBits() & 0xFFFFL,
                uuid.getLeastSignificantBits() >> 48 & 0xFFFFL,
                uuid.getLeastSignificantBits() & 0xFFFFFFFFFFFFL);
    }

    @Transactional(readOnly = true)
    public Page<EdgeItemDto> listEdges(EdgeListQuery query, Pageable pageable) {
        Page<EdgeEntity> pageParam;
        if (pageable != null) {
            pageParam = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
            // TODO: 这里可以根据 pageable.getSort() 来处理排序
        } else {
            pageParam = new Page<>(1, 20);
        }

        Page<EdgeEntity> result = edgeMapper.selectPageWithRegion(pageParam, query.getCityId(), query.getRegionId(), query.getCountryId());
        if (result == null) {
            // 返回空的分页结果
            return new Page<>(pageable.getPageNumber(), pageable.getPageSize(), 0);
        }

        Page<EdgeItemDto> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream()
                .map(edgeConverter::toItemDto)
                .toList());

        return dtoPage;
    }

    @Transactional(readOnly = true)
    public EdgeDetailDto getEdgeDetail(String edgeTag) {
        UUID tag;
        try {
            tag = UUID.fromString(edgeTag);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid edgeTag format");
        }

        EdgeEntity e = edgeMapper.selectOne(new LambdaQueryWrapper<EdgeEntity>()
                .eq(EdgeEntity::getEdgeTag, tag));
        if (e == null) {
            throw new IllegalArgumentException("Edge not found");
        }

        EdgeDetailDto detailDto = edgeConverter.toDetailDto(e);

        EdgeAreaEntity region = regionMapper.selectById(e.getRegionId());
        if (region != null) {
            detailDto.setRegion(new RegionTreeDto()
                    .setId(region.getId())
                    .setName(region.getName())
                    .setLevel(region.getLevel()));
        }

        // 查询网卡列表
        List<EdgeNicEntity> nics = nicMapper.selectList(new LambdaQueryWrapper<EdgeNicEntity>()
                .eq(EdgeNicEntity::getEdgeId, e.getId()));

        // 构建网卡 DTO 列表（包含每个网卡的 IP）
        List<EdgeNicDto> nicDtos = buildNicDtos(nics);

        // 提取能力详情（包含启用/禁用状态）
        List<CapabilityDto> capabilityDetails = extractCapabilityDetails(e.getFeatures());

        return detailDto
                .setCapabilities(capabilityDetails)
                .setNics(nicDtos);
    }

    /**
     * 构建网卡 DTO 列表（包含每个网卡的 IP）
     */
    private List<EdgeNicDto> buildNicDtos(List<EdgeNicEntity> nics) {
        return nics.stream()
                .map(nic -> {
                    EdgeNicDto nicDto = nicConverter.toNicDto(nic);
                    // 查询该网卡的所有 IP
                    List<EdgeNicIpEntity> ips = nicIpMapper.selectList(
                            new LambdaQueryWrapper<EdgeNicIpEntity>()
                                    .eq(EdgeNicIpEntity::getNicId, nic.getId())
                                    .isNull(EdgeNicIpEntity::getRemovedAt)
                    );

                    // 转换为 NicIpDto
                    List<NicIpDto> ipDtos = ips.stream()
                            .map(nicConverter::toIpDto)
                            .toList();

                    return nicDto.setIps(ipDtos);
                })
                .toList();
    }

    /**
     * 从 EdgeFeatures 中提取能力详情（包含启用/禁用状态）
     */
    private List<CapabilityDto> extractCapabilityDetails(EdgeFeatures capabilities) {
        if (capabilities == null || capabilities.getFeatures() == null) {
            return Collections.emptyList();
        }

        return capabilities.getFeatures().entrySet().stream()
                .map(entry -> new CapabilityDto()
                        .setName(entry.getKey())
                        .setEnabled(entry.getValue().isEnabled())
                        .setConfig(entry.getValue().getConfig()))
                .collect(Collectors.toList());
    }

    /**
     * 从 EdgeFeatures 中提取已启用的能力名称
     */
    private Set<String> extractEnabledCapabilities(EdgeFeatures capabilities) {
        if (capabilities == null || capabilities.getFeatures() == null) {
            return Collections.emptySet();
        }

        return capabilities.getFeatures().entrySet().stream()
                .filter(entry -> entry.getValue().isEnabled())
                .map(entry -> entry.getKey())
                .collect(Collectors.toSet());
    }
}