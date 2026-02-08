package com.mikou.edgecloud.eop.application;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.common.spi.ResourceValidator;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopAppEntity;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopAppMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EopResourceValidator implements ResourceValidator {

    private final EopAppMapper eopAppMapper;

    public EopResourceValidator(EopAppMapper eopAppMapper) {
        this.eopAppMapper = eopAppMapper;
    }

    @Override
    public boolean exists(UUID resourceTag) {
        return eopAppMapper.selectCount(new LambdaQueryWrapper<EopAppEntity>()
                .eq(EopAppEntity::getEopTag, resourceTag)) > 0;
    }

    @Override
    public String getSupportedType() {
        return "EOP";
    }
}