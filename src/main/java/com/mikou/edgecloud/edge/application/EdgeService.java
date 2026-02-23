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
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeStatusEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicIpMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeAreaMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeStatusMapper;

import java.time.Instant;
import java.util.*;
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
    private final EdgeStatusMapper edgeStatusMapper;
    private final EdgeRegistry edgeRegistry;
    private final EdgeConverter edgeConverter;
    private final EdgeNicConverter nicConverter;

    public EdgeService(EdgeMapper edgeMapper, EdgeNicMapper nicMapper, EdgeNicIpMapper nicIpMapper,
                       EdgeAreaMapper regionMapper, EdgeStatusMapper edgeStatusMapper,
                       EdgeRegistry edgeRegistry, EdgeConverter edgeConverter, EdgeNicConverter nicConverter) {
        this.edgeMapper = edgeMapper;
        this.nicMapper = nicMapper;
        this.nicIpMapper = nicIpMapper;
        this.regionMapper = regionMapper;
        this.edgeStatusMapper = edgeStatusMapper;
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

        // 批量查询区域信息
        List<Integer> regionIds = result.getRecords().stream()
                .map(EdgeEntity::getRegionId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());
        
        Map<Integer, EdgeAreaEntity> regionMap = Collections.emptyMap();
        if (!regionIds.isEmpty()) {
            regionMap = regionMapper.selectBatchIds(regionIds).stream()
                    .collect(Collectors.toMap(EdgeAreaEntity::getId, r -> r));
        }
        
        final Map<Integer, EdgeAreaEntity> finalRegionMap = regionMap;

        Page<EdgeItemDto> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream()
                .map(entity -> {
                    EdgeItemDto dto = edgeConverter.toItemDto(entity);
                    
                    // 填充区域信息
                    if (entity.getRegionId() != null) {
                        EdgeAreaEntity regionEntity = finalRegionMap.get(entity.getRegionId());
                        if (regionEntity != null) {
                            RegionTreeDto regionDto = new RegionTreeDto()
                                    .setId(regionEntity.getId())
                                    .setCode(regionEntity.getCode())
                                    .setName(regionEntity.getName())
                                    .setLevel(regionEntity.getLevel());
                            dto.setRegion(regionDto);
                        }
                    }
                    
                    // 如果在线，查询最新的状态统计信息
                    if (dto.isOnline() && entity.getId() != null) {
                        EdgeStatusEntity statusEntity = edgeStatusMapper.findLatestByEdgeId(entity.getId());
                        if (statusEntity != null) {
                            EdgeStatusDto statusDto = new EdgeStatusDto()
                                    .setSampleTime(statusEntity.getSampleTime())
                                    .setCpuUsage(statusEntity.getCpuUsage())
                                    .setUsedMemory(statusEntity.getUsedMemory())
                                    .setTotalMemory(statusEntity.getTotalMemory());
                            dto.setLatestStatus(statusDto);
                        }
                    }
                    
                    return dto;
                })
                .toList());

        return dtoPage;
    }

    @Transactional(readOnly = true)
    public EdgeDetailDto getEdgeDetail(Integer edgeId) {
        if (edgeId == null) {
            throw new IllegalArgumentException("edgeId cannot be null");
        }

        EdgeEntity e = edgeMapper.selectById(edgeId);
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

    /**
     * 更新 Edge 节点的硬件和系统信息
     * 通常在 Edge 节点注册或心跳时调用
     */
    @Transactional
    public void updateEdgeInfo(Integer edgeId, UpdateEdgeInfoRequest req) {
        if (edgeId == null) {
            throw new IllegalArgumentException("edgeId cannot be null");
        }

        EdgeEntity edge = edgeMapper.selectById(edgeId);
        if (edge == null) {
            throw new IllegalArgumentException("Edge not found");
        }

        edge.setCpuCores(req.getCpuCores())
            .setCpuModel(req.getCpuModel())
            .setTotalMemory(req.getTotalMemory())
            .setOsType(req.getOsType())
            .setOsVersion(req.getOsVersion())
            .setOsArch(req.getOsArch())
            .setEdgeVersion(req.getEdgeVersion())
            .setUpdatedAt(Instant.now());

        edgeMapper.updateById(edge);
    }

    /**
     * 获取 Edge 节点的系统信息
     * 包含硬件配置、操作系统和 Edge 程序版本
     */
    @Transactional(readOnly = true)
    public EdgeSystemInfoDto getEdgeSystemInfo(Integer edgeId) {
        if (edgeId == null) {
            throw new IllegalArgumentException("edgeId cannot be null");
        }

        EdgeEntity edge = edgeMapper.selectById(edgeId);
        if (edge == null) {
            throw new IllegalArgumentException("Edge not found");
        }

        return new EdgeSystemInfoDto()
                .setEdgeId(edge.getId())
                .setCpuCores(edge.getCpuCores())
                .setCpuModel(edge.getCpuModel())
                .setTotalMemory(edge.getTotalMemory())
                .setOsType(edge.getOsType())
                .setOsVersion(edge.getOsVersion())
                .setOsArch(edge.getOsArch())
                .setEdgeVersion(edge.getEdgeVersion())
                .setUpdatedAt(edge.getUpdatedAt());
    }

    /**
     * 获取 Edge 节点的监控指标时序数据
     * 包含 CPU 和内存使用情况
     * 智能采样策略：
     * - 24小时内：每小时采样
     * - 10天内：每12小时采样
     * - 30天或更长：每天采样
     * 采样方式：取最大值（用于问题排查）
     * 
     * @param edgeId Edge 节点 ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param limit 已废弃，使用智能采样策略
     * @return 监控指标时序数据
     */
    @Transactional(readOnly = true)
    public EdgeMetricsResponse getEdgeMetrics(Integer edgeId, Instant startTime, Instant endTime, Integer limit) {
        if (edgeId == null) {
            throw new IllegalArgumentException("edgeId cannot be null");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("startTime and endTime cannot be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("startTime must be before endTime");
        }

        // 查询 Edge 基本信息
        EdgeEntity edge = edgeMapper.selectById(edgeId);
        if (edge == null) {
            throw new IllegalArgumentException("Edge not found");
        }

        // 计算时间范围（小时）
        long durationSeconds = endTime.getEpochSecond() - startTime.getEpochSecond();
        long durationHours = durationSeconds / 3600;
        
        // 智能采样策略（优化）：根据时间范围自动选择采样间隔
        String samplingInterval;
        int expectedPoints;
        
        if (durationHours <= 1) {
            // 1小时内：每5分钟 (12个点)
            samplingInterval = "5 minutes";
            expectedPoints = 12;
        } else if (durationHours <= 6) {
            // 6小时内：每10分钟 (36个点)
            samplingInterval = "10 minutes";
            expectedPoints = 36;
        } else if (durationHours <= 24) {
            // 24小时内：每30分钟 (48个点)
            samplingInterval = "30 minutes";
            expectedPoints = 48;
        } else if (durationHours <= 168) {
            // 7天内：每1小时 (168个点)
            samplingInterval = "1 hour";
            expectedPoints = (int) durationHours;
        } else if (durationHours <= 720) {
            // 30天内：每2小时 (360个点，21天=252个点)
            samplingInterval = "2 hours";
            expectedPoints = (int) (durationHours / 2);
        } else {
            // 更长时间：每6小时
            samplingInterval = "6 hours";
            expectedPoints = (int) (durationHours / 6);
        }

        System.out.println("=== getEdgeMetrics 智能采样 ===");
        System.out.println("时间范围: " + durationHours + "h (" + String.format("%.1f", durationHours / 24.0) + "天)");
        System.out.println("采样间隔: " + samplingInterval + " (预期" + expectedPoints + "个点)");


        // 使用智能采样查询
        List<EdgeStatusEntity> statusList = edgeStatusMapper.findByEdgeIdAndTimeRangeWithSampling(
                edgeId, startTime, endTime, samplingInterval);

        System.out.println("实际返回: " + (statusList != null ? statusList.size() : 0) + " 个点");
        System.out.println("================================");

        // 转换为 DTO
        List<EdgeMetricsPointDto> metrics = statusList.stream()
                .map(status -> new EdgeMetricsPointDto()
                        .setTimestamp(status.getSampleTime())
                        .setCpuUsage(status.getCpuUsage())  // CPU 已经是百分比 0-100
                        .setMemoryUsage(status.getUsedMemory() != null ? status.getUsedMemory().doubleValue() : 0.0)) // 内存占用率百分比
                .collect(Collectors.toList());

        return new EdgeMetricsResponse()
                .setEdgeId(edge.getId())
                .setEdgeName(edge.getName())
                .setMetrics(metrics);
    }
}
