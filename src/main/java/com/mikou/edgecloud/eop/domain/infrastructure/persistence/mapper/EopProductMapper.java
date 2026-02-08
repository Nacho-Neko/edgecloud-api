package com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopProductEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EopProductMapper extends BaseMapper<EopProductEntity> {
}