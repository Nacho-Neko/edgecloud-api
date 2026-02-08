package com.mikou.edgecloud.eop.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.eop.api.dto.CreateEopProductRequest;
import com.mikou.edgecloud.eop.api.dto.EopProductDto;
import com.mikou.edgecloud.eop.api.dto.UpdateEopProductRequest;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopProductEntity;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopProductMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EopProductService {

    private final EopProductMapper productMapper;

    public EopProductService(EopProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Transactional
    public UUID create(CreateEopProductRequest req) {
        validateCreate(req);

        Instant now = Instant.now();
        EopProductEntity entity = new EopProductEntity()
                .setTag(UUID.randomUUID())
                .setName(req.getName().trim())
                .setDescription(req.getDescription() != null ? req.getDescription().trim() : null)
                .setIntroduction(req.getIntroduction())
                .setMonthlyPrice(req.getMonthlyPrice())
                .setMaxOutboundCount(req.getMaxOutboundCount())
                .setMaxInboundCount(req.getMaxInboundCount())
                .setOutboundMaxConnections(req.getOutboundMaxConnections())
                .setInboundMaxConnections(req.getInboundMaxConnections())
                .setAllowedEntryLevel(req.getAllowedEntryLevel())
                .setMaxOriginTargets(req.getMaxOriginTargets())
                .setCreatedAt(now)
                .setUpdatedAt(now);

        productMapper.insert(entity);
        return entity.getTag();
    }

    @Transactional
    public void updateByTag(String tag, UpdateEopProductRequest req) {
        if (tag == null || tag.isBlank()) {
            throw new IllegalArgumentException("Missing product tag");
        }
        validateUpdate(req);

        EopProductEntity entity = productMapper.selectOne(new LambdaQueryWrapper<EopProductEntity>()
                .eq(EopProductEntity::getTag, tag));
        if (entity == null) {
            throw new IllegalArgumentException("Product not found: " + tag);
        }

        if (req.getName() != null) entity.setName(req.getName().trim());
        if (req.getDescription() != null) entity.setDescription(req.getDescription().trim());
        if (req.getIntroduction() != null) entity.setIntroduction(req.getIntroduction());
        if (req.getMonthlyPrice() != null) entity.setMonthlyPrice(req.getMonthlyPrice());
        if (req.getMaxOutboundCount() != null) entity.setMaxOutboundCount(req.getMaxOutboundCount());
        if (req.getMaxInboundCount() != null) entity.setMaxInboundCount(req.getMaxInboundCount());
        if (req.getOutboundMaxConnections() != null) entity.setOutboundMaxConnections(req.getOutboundMaxConnections());
        if (req.getInboundMaxConnections() != null) entity.setInboundMaxConnections(req.getInboundMaxConnections());
        if (req.getAllowedEntryLevel() != null) entity.setAllowedEntryLevel(req.getAllowedEntryLevel());
        if (req.getMaxOriginTargets() != null) entity.setMaxOriginTargets(req.getMaxOriginTargets());

        entity.setUpdatedAt(Instant.now());
        productMapper.updateById(entity);
    }

    @Transactional
    public void deleteByTag(String tag) {
        if (tag == null || tag.isBlank()) {
            throw new IllegalArgumentException("Missing product tag");
        }

        EopProductEntity entity = productMapper.selectOne(new LambdaQueryWrapper<EopProductEntity>()
                .eq(EopProductEntity::getTag, tag));
        if (entity == null) {
            return; // 幂等：重复删除不报错
        }

        productMapper.deleteById(entity.getId());
    }

    public List<EopProductDto> listAll() {
        return productMapper.selectList(new LambdaQueryWrapper<EopProductEntity>()
                        .isNull(EopProductEntity::getRemovedAt)
                        .orderByAsc(EopProductEntity::getId))
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private EopProductDto toDto(EopProductEntity entity) {
        if (entity == null) return null;
        return new EopProductDto()
                .setTag(entity.getTag())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setIntroduction(entity.getIntroduction())
                .setMonthlyPrice(entity.getMonthlyPrice())
                .setMaxOutboundCount(entity.getMaxOutboundCount())
                .setMaxInboundCount(entity.getMaxInboundCount())
                .setOutboundMaxConnections(entity.getOutboundMaxConnections())
                .setInboundMaxConnections(entity.getInboundMaxConnections())
                .setAllowedEntryLevel(entity.getAllowedEntryLevel())
                .setMaxOriginTargets(entity.getMaxOriginTargets());
    }

    private void validateCreate(CreateEopProductRequest req) {
        // ... existing code ...
        if (req == null) throw new IllegalArgumentException("Missing body");
        if (req.getName() == null || req.getName().isBlank()) throw new IllegalArgumentException("Missing name");
        if (req.getMonthlyPrice() == null || req.getMonthlyPrice().signum() < 0) throw new IllegalArgumentException("Invalid monthlyPrice");

        requirePositive("maxOutboundCount", req.getMaxOutboundCount());
        requirePositive("maxInboundCount", req.getMaxInboundCount());
        requirePositive("outboundMaxConnections", req.getOutboundMaxConnections());
        requirePositive("inboundMaxConnections", req.getInboundMaxConnections());
        requireNonNegative("allowedEntryLevel", req.getAllowedEntryLevel());
        requirePositive("maxOriginTargets", req.getMaxOriginTargets());
    }

    private void validateUpdate(UpdateEopProductRequest req) {
        // ... existing code ...
        if (req == null) throw new IllegalArgumentException("Missing body");
        if (req.getName() != null && req.getName().isBlank()) throw new IllegalArgumentException("Invalid name");
        if (req.getMonthlyPrice() != null && req.getMonthlyPrice().signum() < 0) throw new IllegalArgumentException("Invalid monthlyPrice");

        if (req.getMaxOutboundCount() != null) requirePositive("maxOutboundCount", req.getMaxOutboundCount());
        if (req.getMaxInboundCount() != null) requirePositive("maxInboundCount", req.getMaxInboundCount());
        if (req.getOutboundMaxConnections() != null) requirePositive("outboundMaxConnections", req.getOutboundMaxConnections());
        if (req.getInboundMaxConnections() != null) requirePositive("inboundMaxConnections", req.getInboundMaxConnections());
        if (req.getAllowedEntryLevel() != null) requireNonNegative("allowedEntryLevel", req.getAllowedEntryLevel());
        if (req.getMaxOriginTargets() != null) requirePositive("maxOriginTargets", req.getMaxOriginTargets());
    }

    // ... existing code ...
    private void requirePositive(String name, Integer v) {
        if (v == null || v <= 0) throw new IllegalArgumentException("Invalid " + name);
    }

    private void requireNonNegative(String name, Integer v) {
        if (v == null || v < 0) throw new IllegalArgumentException("Invalid " + name);
    }
}