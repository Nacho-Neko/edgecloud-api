package com.mikou.edgecloud.edge.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.edge.api.dto.EdgeProtocolDto;
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
                                          Integer port, Integer portRangeStart, Integer portRangeEnd) {
        if (ipId == null) throw new IllegalArgumentException("ipId is required");
        if (protocol == null || protocol.isBlank()) throw new IllegalArgumentException("protocol is required");
        if (port == null && (portRangeStart == null || portRangeEnd == null))
            throw new IllegalArgumentException("port or portRange is required");

        validateIpBelongsToEdge(ipId, edgeTag);

        // 校验单端口未被占用
        if (port != null) {
            Long occupied = portOccupationMapper.selectCount(
                    new LambdaQueryWrapper<EdgeIpPortOccupationEntity>()
                            .eq(EdgeIpPortOccupationEntity::getIpId, ipId)
                            .eq(EdgeIpPortOccupationEntity::getPort, port));
            if (occupied > 0) throw new IllegalStateException("Port " + port + " on IP " + ipId + " is already occupied");
        }

        EdgeProtocolEntity entity = new EdgeProtocolEntity()
                .setEdgeTag(edgeTag)
                .setProtocol(protocol)
                .setIpId(ipId)
                .setPort(port)
                .setPortRangeStart(portRangeStart)
                .setPortRangeEnd(portRangeEnd)
                .setCreatedAt(Instant.now())
                .setUpdatedAt(Instant.now());

        edgeProtocolMapper.insert(entity);

        // 登记端口占用，occupier_id = edge_protocol.id
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

        // 释放端口占用记录
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

        return new EdgeProtocolDto()
                .setId(e.getId())
                .setEdgeTag(e.getEdgeTag())
                .setProtocol(e.getProtocol())
                .setIpId(e.getIpId())
                .setIp(ipStr)
                .setPort(e.getPort())
                .setPortRangeStart(e.getPortRangeStart())
                .setPortRangeEnd(e.getPortRangeEnd())
                .setCreatedAt(e.getCreatedAt());
    }
}