
package com.mikou.edgecloud.edge.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeStatusEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EdgeStatusMapper extends BaseMapper<EdgeStatusEntity> {

    @Select("""
        SELECT DISTINCT ON (edge_id) *
        FROM edge_status
        WHERE edge_id = #{edgeId}
        ORDER BY edge_id, sample_time DESC
        """)
    EdgeStatusEntity findLatestByEdgeId(@Param("edgeId") Integer edgeId);
}