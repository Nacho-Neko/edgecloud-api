package com.mikou.edgecloud.edge.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.edge.api.dto.EdgeProtocolDto;
import com.mikou.edgecloud.edge.domain.enums.EdgeProtocolType;
import com.mikou.edgecloud.edge.domain.enums.IpPortOccupierType;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.*;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EdgeProtocolServiceImpl implements EdgeProtocolService {

    private final EdgeProtocolMapper edgeProtocolMapper;
    private final EdgeNicIpMapper edgeNicIpMapper;
    private final EdgeNicMapper edgeNicMapper;
    private final EdgeMapper edgeMapper;
    private final EdgeIpPortOccupationMapper portOccupationMapper;

    public EdgeProtocolServiceImpl(EdgeProtocolMapper edgeProtocolMapper,
                                   EdgeNicIpMapper edgeNicIpMapper,
                                   EdgeNicMapper edgeNicMapper,
                                   EdgeMapper edgeMapper,
                                   EdgeIpPortOccupationMapper portOccupationMapper) {
        this.edgeProtocolMapper = edgeProtocolMapper;
        this.edgeNicIpMapper = edgeNicIpMapper;
        this.edgeNicMapper = edgeNicMapper;
        this.edgeMapper = edgeMapper;
        this.portOccupationMapper = portOccupationMapper;
    }

    @Override
    @Transactional
    public EdgeProtocolDto createProtocol(UUID edgeTag, String protocol, Integer ipId,
                                          Integer port, String portRange) {
        if (ipId == null) throw new IllegalArgumentException("ipId is required");
        if (protocol == null || protocol.isBlank()) throw new IllegalArgumentException("protocol is required");

        // 解析协议枚举（不支持的协议直接报错）
        EdgeProtocolType protocolType;
        try {
            protocolType = EdgeProtocolType.valueOf(protocol.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported protocol: " + protocol
                    + ". Supported: " + java.util.Arrays.toString(EdgeProtocolType.values()));
        }

        // port 与 portRange 互斥校验
        if (port != null && portRange != null) {
            throw new IllegalArgumentException("port and portRange are mutually exclusive, specify only one");
        }

        // 根据协议类型校验端口模式
        if (protocolType.requiresSinglePort()) {
            if (port == null) throw new IllegalArgumentException(protocol + " requires a single port, not a port range");
            if (portRange != null) throw new IllegalArgumentException(protocol + " does not support port range");
        } else {
            if (portRange == null) throw new IllegalArgumentException(protocol + " requires a port range");
            if (port != null) throw new IllegalArgumentException(protocol + " does not support single port");
        }

        validateIpBelongsToEdge(ipId, edgeTag);

        // 解析 portRange
        Integer portRangeStart = null;
        Integer portRangeEnd = null;
        if (portRange != null) {
            int[] range = parsePortRange(portRange);
            portRangeStart = range[0];
            portRangeEnd = range[1];
        }

        // 单端口占用校验
        if (port != null) {
            Long occupied = portOccupationMapper.selectCount(
                    new LambdaQueryWrapper<EdgeIpPortOccupationEntity>()
                            .eq(EdgeIpPortOccupationEntity::getIpId, ipId)
                            .eq(EdgeIpPortOccupationEntity::getPort, port));
            if (occupied > 0)
                throw new IllegalStateException("Port " + port + " on IP " + ipId + " is already occupied");
        }

        EdgeProtocolEntity entity = new EdgeProtocolEntity()
                .setEdgeTag(edgeTag)
                .setProtocol(protocolType.name())
                .setIpId(ipId)
                .setPort(port)
                .setPortRangeStart(portRangeStart)
                .setPortRangeEnd(portRangeEnd)
                .setCreatedAt(Instant.now())
                .setUpdatedAt(Instant.now());

        edgeProtocolMapper.insert(entity);

        // 登记端口占用（单端口）
        if (port != null) {
            portOccupationMapper.insert(new EdgeIpPortOccupationEntity()
                    .setIpId(ipId)
                    .setPort(port)
                    .setOccupierType(IpPortOccupierType.PROTOCOL)
                    .setOccupierId(entity.getId())
                    .setCreatedAt(Instant.now()));
        }

        return mapToDto(entity);
    }

    @Override
    @Transactional
    public void destroyProtocol(Integer id) {
        EdgeProtocolEntity entity = edgeProtocolMapper.selectById(id);
        if (entity == null) return;

        if (entity.getIpId() != null && entity.getPort() != null) {
            portOccupationMapper.delete(new LambdaQueryWrapper<EdgeIpPortOccupationEntity>()
                    .eq(EdgeIpPortOccupationEntity::getIpId, entity.getIpId())
                    .eq(EdgeIpPortOccupationEntity::getPort, entity.getPort())
                    .eq(EdgeIpPortOccupationEntity::getOccupierType, IpPortOccupierType.PROTOCOL)
                    .eq(EdgeIpPortOccupationEntity::getOccupierId, id));
        }

        edgeProtocolMapper.deleteById(id);
    }

    @Override
    public Page<EdgeProtocolDto> listProtocolsByEdgeTag(UUID edgeTag, Pageable pageable) {
        Page<EdgeProtocolEntity> pageParam = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        Page<EdgeProtocolEntity> result = edgeProtocolMapper.selectPage(pageParam,
                new LambdaQueryWrapper<EdgeProtocolEntity>()
                        .eq(EdgeProtocolEntity::getEdgeTag, edgeTag)
                        .isNull(EdgeProtocolEntity::getRemovedAt));

        Page<EdgeProtocolDto> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream().map(this::mapToDto).collect(Collectors.toList()));
        return dtoPage;
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    /**
     * 解析 "start-end" 格式的端口范围字符串
     */
    private int[] parsePortRange(String portRange) {
        String[] parts = portRange.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid portRange format, expected 'start-end', e.g. '10000-20000'");
        }
        try {
            int start = Integer.parseInt(parts[0].trim());
            int end = Integer.parseInt(parts[1].trim());
            if (start <= 0 || end > 65535 || start >= end) {
                throw new IllegalArgumentException(
                        "Invalid portRange: start must be > 0, end must be <= 65535, and start < end");
            }
            return new int[]{start, end};
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid portRange format, expected 'start-end', e.g. '10000-20000'");
        }
    }

    private void validateIpBelongsToEdge(Integer ipId, UUID edgeTag) {
        EdgeNicIpEntity ip = edgeNicIpMapper.selectById(ipId);
        if (ip == null) throw new IllegalArgumentException("IP not found: " + ipId);

        EdgeNicEntity nic = edgeNicMapper.selectById(ip.getNicId());
        if (nic == null) throw new IllegalArgumentException("NIC not found for IP: " + ipId);

        EdgeEntity edge = edgeMapper.selectById(nic.getEdgeId());
        if (edge == null || !edgeTag.equals(edge.getEdgeTag()))
            throw new IllegalArgumentException("IP " + ipId + " does not belong to Edge " + edgeTag);
    }

    private EdgeProtocolDto mapToDto(EdgeProtocolEntity e) {
        EdgeNicIpEntity ip = edgeNicIpMapper.selectById(e.getIpId());
        String ipStr = ip != null ? (ip.getPublicIp() != null ? ip.getPublicIp() : ip.getPrivateIp()) : null;

        // 将存储的两个字段还原为 "start-end" 格式字符串
        String portRange = null;
        if (e.getPortRangeStart() != null && e.getPortRangeEnd() != null) {
            portRange = e.getPortRangeStart() + "-" + e.getPortRangeEnd();
        }

        return new EdgeProtocolDto()
                .setId(e.getId())
                .setEdgeTag(e.getEdgeTag())
                .setProtocol(e.getProtocol())
                .setIpId(e.getIpId())
                .setIp(ipStr)
                .setPort(e.getPort())
                .setPortRange(portRange)
                .setCreatedAt(e.getCreatedAt());
    }
}
