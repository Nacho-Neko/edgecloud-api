package com.mikou.edgecloud.edge.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeStatusEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.Instant;
import java.util.List;

/**
 * EdgeStatus Mapper
 * edge_status 是时序表，所有 SQL 在 XML 中配置以便优化
 */
@Mapper
public interface EdgeStatusMapper extends BaseMapper<EdgeStatusEntity> {

    /**
     * 查询指定 Edge 的最新一条监控数据
     */
    EdgeStatusEntity findLatestByEdgeId(@Param("edgeId") Integer edgeId);

    /**
     * 查询指定时间范围内的监控数据
     */
    List<EdgeStatusEntity> findByEdgeIdAndTimeRange(
            @Param("edgeId") Integer edgeId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime
    );

    /**
     * 查询指定时间范围内的监控数据（带采样限制）
     * 使用时间桶聚合方式进行采样
     */
    List<EdgeStatusEntity> findByEdgeIdAndTimeRangeWithLimit(
            @Param("edgeId") Integer edgeId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime,
            @Param("limit") Integer limit
    );

    /**
     * 查询指定时间范围内的监控数据（智能采样）
     * 根据时间间隔进行分组，每组取最大值
     * @param samplingInterval PostgreSQL 间隔表达式，如 "1 hour", "12 hours", "1 day"
     */
    List<EdgeStatusEntity> findByEdgeIdAndTimeRangeWithSampling(
            @Param("edgeId") Integer edgeId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime,
            @Param("samplingInterval") String samplingInterval
    );

    /**
     * 查询指定 Edge 的所有监控数据（用于调试，限制100条）
     */
    List<EdgeStatusEntity> findAllByEdgeId(@Param("edgeId") Integer edgeId);

    /**
     * 统计指定 Edge 的监控数据总数
     */
    Long countByEdgeId(@Param("edgeId") Integer edgeId);
    
    /**
     * 统计指定时间范围内的数据点数量
     */
    Long countByEdgeIdAndTimeRange(
            @Param("edgeId") Integer edgeId,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime
    );
}