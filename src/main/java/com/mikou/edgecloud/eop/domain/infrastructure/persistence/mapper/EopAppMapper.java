package com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopAppEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EopAppMapper extends BaseMapper<EopAppEntity> {
    Page<EopAppEntity> selectPageWithRegion(Page<EopAppEntity> page,
                                           @Param("cityId") Integer cityId,
                                           @Param("regionId") Integer regionId,
                                           @Param("countryId") Integer countryId);
}