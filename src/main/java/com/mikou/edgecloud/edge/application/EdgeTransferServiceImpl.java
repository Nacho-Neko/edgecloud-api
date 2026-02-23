package com.mikou.edgecloud.edge.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.common.spi.NatsClient;
import com.mikou.edgecloud.edge.api.dto.EdgeProtocolDto;
import com.mikou.edgecloud.edge.api.dto.EdgeTransferDto;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicIpEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeProtocolEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeTransferEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicIpMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeProtocolMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeTransferMapper;
import com.mikou.edgecloud.business.eop.domain.events.EopNotifyMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EdgeTransferServiceImpl implements EdgeTransferService {

    private final EdgeTransferMapper edgeTransferMapper;
    private final EdgeProtocolMapper edgeProtocolMapper;
    private final EdgeMapper edgeMapper;
    private final EdgeNicMapper edgeNicMapper;
    private final EdgeNicIpMapper edgeNicIpMapper;
    private final NatsClient natsClient;

    public EdgeTransferServiceImpl(EdgeTransferMapper edgeTransferMapper,
                                   EdgeProtocolMapper edgeProtocolMapper,
                                   EdgeMapper edgeMapper,
                                   EdgeNicMapper edgeNicMapper,
                                   EdgeNicIpMapper edgeNicIpMapper,
                                   NatsClient natsClient) {
        this.edgeTransferMapper = edgeTransferMapper;
        this.edgeProtocolMapper = edgeProtocolMapper;
        this.edgeMapper = edgeMapper;
        this.edgeNicMapper = edgeNicMapper;
        this.edgeNicIpMapper = edgeNicIpMapper;
        this.natsClient = natsClient;
    }

    @Override
    @Transactional
    public EdgeTransferDto createTransfer(UUID senderEdgeTag, Integer sendIpId,
                                          Integer targetProtocolId, String hostName, String remark) {
        if (sendIpId == null)         throw new IllegalArgumentException("sendIpId is required");
        if (targetProtocolId == null) throw new IllegalArgumentException("targetProtocolId is required");
        if (hostName == null || hostName.isBlank()) throw new IllegalArgumentException("hostName is required");

        // 校验 senderEdge 存在
        EdgeEntity senderEdge = findEdgeByTag(senderEdgeTag);

        // 校验发送端 IP 归属 senderEdge
        validateIpBelongsToEdge(sendIpId, senderEdge.getId(), senderEdgeTag);

        // 查询目标监听器
        EdgeProtocolEntity protocol = edgeProtocolMapper.selectById(targetProtocolId);
        if (protocol == null) throw new IllegalArgumentException("Protocol not found: " + targetProtocolId);

        EdgeTransferEntity entity = new EdgeTransferEntity()
                .setPairKey(UUID.randomUUID())
                .setSenderEdgeTag(senderEdgeTag)
                .setSendIpId(sendIpId)
                .setTargetEdgeTag(protocol.getEdgeTag())
                .setTargetProtocolId(targetProtocolId)
                .setHostName(hostName)
                .setRemark(remark)
                .setCreatedAt(Instant.now())
                .setUpdatedAt(Instant.now());

        edgeTransferMapper.insert(entity);

        EdgeTransferDto dto = mapToDto(entity, protocol);
        notifyNats(EopNotifyMessage.ACTION_CREATE_TRANSFER, senderEdgeTag, dto);
        notifyNats(EopNotifyMessage.ACTION_CREATE_TRANSFER, protocol.getEdgeTag(), dto);
        return dto;
    }

    @Override
    @Transactional
    public void deleteTransfer(UUID pairKey) {
        EdgeTransferEntity entity = edgeTransferMapper.selectOne(
                new LambdaQueryWrapper<EdgeTransferEntity>()
                        .eq(EdgeTransferEntity::getPairKey, pairKey));
        if (entity == null) return;

        edgeTransferMapper.deleteById(entity.getId());
        notifyNats(EopNotifyMessage.ACTION_DELETE_TRANSFER, entity.getSenderEdgeTag(), entity);
        notifyNats(EopNotifyMessage.ACTION_DELETE_TRANSFER, entity.getTargetEdgeTag(), entity);
    }

    @Override
    public Page<EdgeTransferDto> listTransfersByEdgeTag(UUID edgeTag, Pageable pageable) {
        Page<EdgeTransferEntity> pageParam = new Page<>(
                pageable.getPageNumber() + 1, pageable.getPageSize());

        Page<EdgeTransferEntity> result = edgeTransferMapper.selectPage(pageParam,
                new LambdaQueryWrapper<EdgeTransferEntity>()
                        .eq(EdgeTransferEntity::getSenderEdgeTag, edgeTag)
                        .or()
                        .eq(EdgeTransferEntity::getTargetEdgeTag, edgeTag));

        Page<EdgeTransferDto> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream()
                .map(e -> {
                    EdgeProtocolEntity protocol = edgeProtocolMapper.selectById(e.getTargetProtocolId());
                    return mapToDto(e, protocol);
                })
                .collect(Collectors.toList()));
        return dtoPage;
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private EdgeEntity findEdgeByTag(UUID edgeTag) {
        EdgeEntity edge = edgeMapper.selectOne(
                new LambdaQueryWrapper<EdgeEntity>().eq(EdgeEntity::getEdgeTag, edgeTag));
        if (edge == null) throw new IllegalArgumentException("Edge not found: " + edgeTag);
        return edge;
    }

    private void validateIpBelongsToEdge(Integer ipId, Integer edgeId, UUID edgeTag) {
        EdgeNicIpEntity ip = edgeNicIpMapper.selectById(ipId);
        if (ip == null) throw new IllegalArgumentException("IP not found: " + ipId);
        EdgeNicEntity nic = edgeNicMapper.selectById(ip.getNicId());
        if (nic == null || !edgeId.equals(nic.getEdgeId()))
            throw new IllegalArgumentException("IP " + ipId + " does not belong to Edge " + edgeTag);
    }

    private EdgeTransferDto mapToDto(EdgeTransferEntity e, EdgeProtocolEntity protocol) {
        EdgeProtocolDto protocolDto = null;
        if (protocol != null) {
            EdgeNicIpEntity protocolIp = edgeNicIpMapper.selectById(protocol.getIpId());
            String ipStr = protocolIp != null
                    ? (protocolIp.getPublicIp() != null ? protocolIp.getPublicIp() : protocolIp.getPrivateIp())
                    : null;
            protocolDto = new EdgeProtocolDto()
                    .setId(protocol.getId())
                    .setEdgeTag(protocol.getEdgeTag())
                    .setProtocol(protocol.getProtocol())
                    .setIpId(protocol.getIpId())
                    .setIp(ipStr)
                    .setPort(protocol.getPort())
                    .setPortRangeStart(protocol.getPortRangeStart())
                    .setPortRangeEnd(protocol.getPortRangeEnd());
        }

        EdgeNicIpEntity sendIp = edgeNicIpMapper.selectById(e.getSendIpId());
        String sendIpStr = sendIp != null
                ? (sendIp.getPublicIp() != null ? sendIp.getPublicIp() : sendIp.getPrivateIp())
                : null;

        return new EdgeTransferDto()
                .setPairKey(e.getPairKey())
                .setSenderEdgeTag(e.getSenderEdgeTag())
                .setSendIpId(e.getSendIpId())
                .setSendIp(sendIpStr)
                .setTargetProtocol(protocolDto)
                .setHostName(e.getHostName())
                .setRemark(e.getRemark())
                .setCreatedAt(e.getCreatedAt());
    }

    private void notifyNats(String action, UUID edgeTag, Object data) {
        natsClient.publish("eop.notify", new EopNotifyMessage(action, edgeTag, data));
    }
}