package com.mikou.edgecloud.eop.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.common.enums.FeatureType;
import com.mikou.edgecloud.common.spi.NatsClient;
import com.mikou.edgecloud.edge.domain.service.IpAllocationService;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicIpEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeMapper;
import com.mikou.edgecloud.eop.domain.enums.EopOccupierType;
import com.mikou.edgecloud.eop.domain.events.EopNotifyMessage;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopAppEntity;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopAppMapper;
import com.mikou.edgecloud.eop.domain.model.EopSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EopProtocolService {

    private final EopAppMapper eopAppMapper;
    private final IpAllocationService ipAllocationService;
    private final EopPortService eopPortService;
    private final EdgeMapper edgeMapper;
    private final NatsClient natsClient;

    public EopProtocolService(EopAppMapper eopAppMapper, IpAllocationService ipAllocationService, EopPortService eopPortService, EdgeMapper edgeMapper, NatsClient natsClient) {
        this.eopAppMapper = eopAppMapper;
        this.ipAllocationService = ipAllocationService;
        this.eopPortService = eopPortService;
        this.edgeMapper = edgeMapper;
        this.natsClient = natsClient;
    }

    @Transactional
    public void createProtocol(UUID eopTag, String protocol, EopSettings.ProtocolListener listener) {
        EopAppEntity eop = eopAppMapper.selectOne(new LambdaQueryWrapper<EopAppEntity>()
                .eq(EopAppEntity::getEopTag, eopTag));
        if (eop == null) {
            throw new IllegalArgumentException("EOP not found: " + eopTag);
        }

        Integer ipId = listener.getListenIp();
        if (ipId == null) {
            throw new IllegalArgumentException("Listen IP is required");
        }

        // 校验 IP 是否分配给当前 EOP
        ipAllocationService.findAllocatedIps(FeatureType.EOP.name(), eopTag)
                .stream()
                .filter(i -> i.getId().equals(ipId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("IP " + ipId + " not allocated to this EOP"));

        EopSettings settings = eop.getSettings();
        if (settings == null) {
            settings = new EopSettings();
        }

        // 注册端口占用 (通过领域事件发起申请)
        if (listener.getPort() != null) {
            eopPortService.applyPort(ipId, listener.getPort(), EopOccupierType.PROTOCOL, eop.getId());
        }

        settings.addListener(protocol, listener);
        eop.setSettings(settings);
        eopAppMapper.updateById(eop);

        // 通知 NATS
        notifyNats(EopNotifyMessage.ACTION_UPDATE_SETTINGS, getEdgeTag(eop.getEdgeId()), eop.getSettings());
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
    public void destroyProtocol(UUID eopTag, String protocol, Integer port) {
        EopAppEntity eop = eopAppMapper.selectOne(new LambdaQueryWrapper<EopAppEntity>()
                .eq(EopAppEntity::getEopTag, eopTag));
        if (eop == null) {
            throw new IllegalArgumentException("EOP not found: " + eopTag);
        }

        EopSettings settings = eop.getSettings();
        if (settings == null || settings.getListeners() == null) {
            return;
        }

        EopSettings.ProtocolListener listener = settings.getListeners().get(protocol);
        if (listener != null && Objects.equals(listener.getPort(), port)) {
            if (listener.getListenIp() != null && listener.getPort() != null) {
                // 释放端口占用
                eopPortService.requestReleasePort(listener.getListenIp(), listener.getPort());
            }
            settings.getListeners().remove(protocol);
        }

        eop.setSettings(settings);
        eopAppMapper.updateById(eop);

        // 通知 NATS
        notifyNats(EopNotifyMessage.ACTION_UPDATE_SETTINGS, getEdgeTag(eop.getEdgeId()), eop.getSettings());
    }

    @Transactional
    public void updateSettings(UUID eopTag, EopSettings newSettings) {
        EopAppEntity eop = eopAppMapper.selectOne(new LambdaQueryWrapper<EopAppEntity>()
                .eq(EopAppEntity::getEopTag, eopTag));
        if (eop == null) {
            throw new IllegalArgumentException("EOP not found: " + eopTag);
        }

        EopSettings currentSettings = eop.getSettings();
        if (currentSettings == null) {
            currentSettings = new EopSettings();
        }

        // 仅更新允许的端口范围等非协议监听器设置
        currentSettings.setAllowedPortRange(newSettings.getAllowedPortRange());

        eop.setSettings(currentSettings);
        eop.setUpdatedAt(java.time.Instant.now());
        eopAppMapper.updateById(eop);

        // 通知 NATS
        notifyNats(EopNotifyMessage.ACTION_UPDATE_SETTINGS, getEdgeTag(eop.getEdgeId()), eop.getSettings());
    }

    private java.util.Optional<Integer> findIpIdByPrivateIp(List<EdgeNicIpEntity> ips, String privateIp) {
        return ips.stream()
                .filter(ip -> Objects.equals(ip.getPrivateIp(), privateIp))
                .map(EdgeNicIpEntity::getId)
                .findFirst();
    }
}