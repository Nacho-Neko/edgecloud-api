package com.mikou.edgecloud.edge.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EdgeMapper extends BaseMapper<EdgeEntity> {

    Page<EdgeEntity> selectPageWithRegion(
            Page<EdgeEntity> page,
            @Param("cityId") Integer cityId,
            @Param("regionId") Integer regionId,
            @Param("countryId") Integer countryId
    );
}