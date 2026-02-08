package com.mikou.edgecloud.eop.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.common.enums.FeatureType;
import com.mikou.edgecloud.common.spi.NatsClient;
import com.mikou.edgecloud.edge.domain.service.IpAllocationService;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicIpEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicIpMapper;
import com.mikou.edgecloud.eop.api.dto.CreateEopTransferRequest;
import com.mikou.edgecloud.eop.api.dto.EopTransferDto;
import com.mikou.edgecloud.eop.domain.events.EopNotifyMessage;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopAppEntity;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopTransferEntity;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopAppMapper;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopTransferMapper;
import com.mikou.edgecloud.eop.domain.model.EopSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EopTransferServiceImpl implements EopTransferService {

    private final EopTransferMapper eopTransferMapper;
    private final EopAppMapper eopAppMapper;
    private final IpAllocationService ipAllocationService;
    private final EdgeNicIpMapper edgeNicIpMapper;
    private final EdgeMapper edgeMapper;
    private final EopPortService eopPortService;
    private final NatsClient natsClient;

    public EopTransferServiceImpl(EopTransferMapper eopTransferMapper, 
                                 EopAppMapper eopAppMapper, 
                                 IpAllocationService ipAllocationService,
                                 EdgeNicIpMapper edgeNicIpMapper,
                                 EdgeMapper edgeMapper,
                                 EopPortService eopPortService,
                                 NatsClient natsClient) {
        this.eopTransferMapper = eopTransferMapper;
        this.eopAppMapper = eopAppMapper;
        this.ipAllocationService = ipAllocationService;
        this.edgeNicIpMapper = edgeNicIpMapper;
        this.edgeMapper = edgeMapper;
        this.eopPortService = eopPortService;
        this.natsClient = natsClient;
    }

    @Override
    @Transactional
    public EopTransferDto createTransfer(String senderEopTag, String targetEopTag, Integer sendIpId, Integer receiveIpId, String protocol, String hostName, String remark) {
        // 0. 基础参数校验
        if (sendIpId == null) {
            throw new IllegalArgumentException("sendIpId cannot be null");
        }
        if (receiveIpId == null) {
            throw new IllegalArgumentException("receiveIpId cannot be null");
        }
        if (hostName == null || hostName.isBlank()) {
            throw new IllegalArgumentException("hostName cannot be empty");
        }
        if (protocol == null || protocol.isBlank()) {
            throw new IllegalArgumentException("protocol cannot be empty");
        }

        // 1. 获取发送方和接收方 EOP
        EopAppEntity senderApp = findEopByTag(senderEopTag);
        EopAppEntity targetApp = findEopByTag(targetEopTag);

        // 2. 校验协议 (Protocol Check)
        // validateProtocol(senderApp, protocol);
        validateProtocol(targetApp, protocol);

        // 3. IP 校验 (IP Check)
        EdgeNicIpEntity sendIpEntity = validateIpAllocation(senderApp.getEopTag(), sendIpId);
        EdgeNicIpEntity receiveIpEntity = validateIpAllocation(targetApp.getEopTag(), receiveIpId);

        // 4. 创建传输实体
        EopTransferEntity entity = new EopTransferEntity()
                .setPairKey(UUID.randomUUID())
                .setSenderEop(senderApp.getEopTag())
                .setTargetEop(targetApp.getEopTag())
                .setSendIpId(sendIpId)
                .setReceiveIpId(receiveIpId)
                .setProtocol(protocol)
                .setHostName(hostName)
                .setRemark(remark)
                .setCreatedAt(Instant.now())
                .setUpdatedAt(Instant.now());

        eopTransferMapper.insert(entity);

        EopTransferDto dto = mapToDto(entity, senderApp.getEopTag(), targetApp.getEopTag(), 
                getIpAddress(sendIpEntity), getIpAddress(receiveIpEntity));

        // 通知 NATS
        notifyNats(EopNotifyMessage.ACTION_CREATE_TRANSFER, getEdgeTag(senderApp.getEdgeId()), dto);
        notifyNats(EopNotifyMessage.ACTION_CREATE_TRANSFER, getEdgeTag(targetApp.getEdgeId()), dto);

        return dto;
    }

    private void notifyNats(String action, UUID edgeTag, Object data) {
        String subject = "eop.notify";
        natsClient.publish(subject, new EopNotifyMessage(action, edgeTag, data));
    }

    @Override
    @Transactional
    public void deleteTransfer(Integer id) {
        EopTransferEntity entity = eopTransferMapper.selectById(id);
        if (entity != null) {
            eopTransferMapper.deleteById(id);
            // 通知 NATS
            EopAppEntity senderApp = eopAppMapper.selectOne(new LambdaQueryWrapper<EopAppEntity>()
                    .eq(EopAppEntity::getEopTag, entity.getSenderEop()));
            EopAppEntity targetApp = eopAppMapper.selectOne(new LambdaQueryWrapper<EopAppEntity>()
                    .eq(EopAppEntity::getEopTag, entity.getTargetEop()));

            if (senderApp != null) {
                notifyNats(EopNotifyMessage.ACTION_DELETE_TRANSFER, getEdgeTag(senderApp.getEdgeId()), entity);
            }
            if (targetApp != null) {
                notifyNats(EopNotifyMessage.ACTION_DELETE_TRANSFER, getEdgeTag(targetApp.getEdgeId()), entity);
            }
        }
    }

    private UUID getEdgeTag(Integer edgeId) {
        if (edgeId == null) return null;
        EdgeEntity edge = edgeMapper.selectById(edgeId);
        return edge != null ? edge.getEdgeTag() : null;
    }

    @Override
    public List<EopTransferDto> listTransfersByEopTag(String eopTag) {
        EopAppEntity app = findEopByTag(eopTag);
        UUID eopUuid = app.getEopTag();
        
        List<EopTransferEntity> entities = eopTransferMapper.selectList(new LambdaQueryWrapper<EopTransferEntity>()
                .eq(EopTransferEntity::getSenderEop, eopUuid)
                .or()
                .eq(EopTransferEntity::getTargetEop, eopUuid));

        return entities.stream()
                .map(entity -> {
                    UUID senderTag = entity.getSenderEop();
                    UUID targetTag = entity.getTargetEop();
                    String sendIpStr = getIpAddressById(entity.getSendIpId());
                    String receiveIpStr = getIpAddressById(entity.getReceiveIpId());
                    return mapToDto(entity, senderTag, targetTag, sendIpStr, receiveIpStr);
                })
                .collect(Collectors.toList());
    }

    private EopAppEntity findEopByTag(String eopTag) {
        EopAppEntity app = eopAppMapper.selectOne(new LambdaQueryWrapper<EopAppEntity>()
                .eq(EopAppEntity::getEopTag, eopTag));
        if (app == null) {
            throw new IllegalArgumentException("EOP not found with tag: " + eopTag);
        }
        return app;
    }

    private String getIpAddressById(Integer ipId) {
        if (ipId == null) return "UNKNOWN";
        EdgeNicIpEntity ipEntity = edgeNicIpMapper.selectById(ipId);
        return getIpAddress(ipEntity);
    }

    private String getIpAddress(EdgeNicIpEntity ipEntity) {
        if (ipEntity == null) return "UNKNOWN";
        return ipEntity.getPublicIp() != null ? ipEntity.getPublicIp() : ipEntity.getPrivateIp();
    }

    private void validateProtocol(EopAppEntity app, String protocol) {
        EopSettings settings = app.getSettings();
        if (settings == null || settings.getListeners() == null) {
            throw new IllegalArgumentException("EOP " + app.getEopTag() + " has no protocols configured");
        }
        boolean supported = settings.getListeners().containsKey(protocol);
        if (!supported) {
            throw new IllegalArgumentException("Protocol " + protocol + " is not supported by EOP " + app.getEopTag());
        }
    }

    private EdgeNicIpEntity validateIpAllocation(UUID eopTag, Integer ipId) {
        List<EdgeNicIpEntity> allocatedIps = ipAllocationService.findAllocatedIps(FeatureType.EOP.name(), eopTag);
        Optional<EdgeNicIpEntity> ipEntity = allocatedIps.stream()
                .filter(ip -> ip.getId().equals(ipId))
                .findFirst();
        
        if (ipEntity.isEmpty()) {
            throw new IllegalArgumentException("IP ID " + ipId + " is not allocated to EOP " + eopTag);
        }
        return ipEntity.get();
    }

    private EopTransferDto mapToDto(EopTransferEntity entity, UUID senderTag, UUID targetTag, String sendIpStr, String receiveIpStr) {
        EopTransferDto dto = new EopTransferDto();
        dto.setId(entity.getId());
        dto.setPairKey(entity.getPairKey());
        dto.setSenderEopTag(senderTag);
        dto.setTargetEopTag(targetTag);
        dto.setSendIpId(entity.getSendIpId());
        dto.setReceiveIpId(entity.getReceiveIpId());
        dto.setSendIp(sendIpStr);
        dto.setReceiveIp(receiveIpStr);
        dto.setProtocol(entity.getProtocol());
        dto.setHostName(entity.getHostName());
        dto.setRemark(entity.getRemark());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
