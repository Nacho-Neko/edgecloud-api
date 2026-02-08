package com.mikou.edgecloud.edge.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.common.enums.FeatureType;
import com.mikou.edgecloud.common.spi.ResourceValidator;
import com.mikou.edgecloud.edge.api.dto.EdgeNicDto;
import com.mikou.edgecloud.edge.api.dto.NicIpDto;
import com.mikou.edgecloud.edge.domain.service.IpAllocationService;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicIpEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicIpMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeNicMapper;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EdgeAssetService {

    private final EdgeMapper edgeMapper;
    private final EdgeNicMapper nicMapper;
    private final EdgeNicIpMapper nicIpMapper;
    private final IpAllocationService ipAllocationService;

    private final ObjectProvider<List<ResourceValidator>> validatorProvider;
    private final Map<String, ResourceValidator> validatorCache = new ConcurrentHashMap<>();

    public EdgeAssetService(EdgeMapper edgeMapper,
                            EdgeNicMapper nicMapper,
                            EdgeNicIpMapper nicIpMapper,
                            IpAllocationService ipAllocationService,
                            ObjectProvider<List<ResourceValidator>> validatorProvider) {
        this.edgeMapper = edgeMapper;
        this.nicMapper = nicMapper;
        this.nicIpMapper = nicIpMapper;
        this.ipAllocationService = ipAllocationService;
        this.validatorProvider = validatorProvider;
    }

    private ResourceValidator getValidator(String type) {
        return validatorCache.computeIfAbsent(type, key -> {
            List<ResourceValidator> localValidators = validatorProvider.getIfAvailable();
            if (localValidators != null) {
                for (ResourceValidator v : localValidators) {
                    if (key.equalsIgnoreCase(v.getSupportedType())) {
                        return v;
                    }
                }
            }
            return null;
        });
    }

    @Transactional
    public void setPublicIp(Integer ipId, String publicIp) {
        EdgeNicIpEntity ip = nicIpMapper.selectById(ipId);
        if (ip == null) {
            throw new IllegalArgumentException("IP not found");
        }
        ip.setPublicIp(publicIp).setUpdatedAt(Instant.now());
        nicIpMapper.updateById(ip);
    }

    @Transactional
    public boolean allocateIp(Integer ipId, FeatureType featureType, UUID resourceTag) {
        String allocationType = featureType.name();

        ResourceValidator validator = getValidator(allocationType);
        if (validator == null) {
            throw new IllegalArgumentException("Unsupported feature type: " + allocationType);
        }

        if (!validator.exists(resourceTag)) {
            throw new IllegalArgumentException(
                    String.format("%s resource not found with tag: %s", allocationType, resourceTag)
            );
        }

        return ipAllocationService.allocateIp(ipId, allocationType, resourceTag);
    }

    @Transactional(readOnly = true)
    public List<EdgeNicDto> getEdgeNics(String edgeTag) {
        EdgeEntity edge = edgeMapper.selectOne(new LambdaQueryWrapper<EdgeEntity>()
                .eq(EdgeEntity::getEdgeTag, edgeTag));
        if (edge == null) {
            throw new IllegalArgumentException("Edge not found");
        }

        List<EdgeNicEntity> nics = nicMapper.selectList(new LambdaQueryWrapper<EdgeNicEntity>()
                .eq(EdgeNicEntity::getEdgeId, edge.getId()));

        return nics.stream().map(nic -> {
            List<EdgeNicIpEntity> ips = nicIpMapper.selectList(new LambdaQueryWrapper<EdgeNicIpEntity>()
                    .eq(EdgeNicIpEntity::getNicId, nic.getId())
                    .isNull(EdgeNicIpEntity::getRemovedAt));

            List<NicIpDto> ipDtos = ips.stream()
                    .map(ip -> new NicIpDto()
                            .setId(ip.getId())
                            .setPrivateIp(ip.getPrivateIp())
                            .setPublicIp(ip.getPublicIp())
                            .setStatus(ip.getStatus())
                            .setAllocated(ip.getAllocatedToId() != null)
                            .setAllocatedToType(ip.getAllocatedToType())
                            .setAllocatedToId(ip.getAllocatedToId()))
                    .toList();

            return new EdgeNicDto()
                    .setMacAddress(nic.getMacAddress())
                    .setNicName(nic.getNicName())
                    .setCreatedAt(nic.getCreatedAt())
                    .setIps(ipDtos);
        }).toList();
    }
}