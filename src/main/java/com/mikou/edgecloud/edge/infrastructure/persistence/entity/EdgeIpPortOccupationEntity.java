package com.mikou.edgecloud.edge.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mikou.edgecloud.edge.domain.enums.IpPortOccupierType;

import java.time.Instant;

/**
 * IP 端口占用记录（上移自 EOP 层，现归属 Edge 层统一管理）
 * 对应表: edge_ip_port_occupation
 * 所有业务（EOP、CDN、Edge传输层）都向此表登记端口占用，互相感知。
 */
@TableName("edge_ip_port_occupation")
public class EdgeIpPortOccupationEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 对应 edge_nic_ip.id */
    @TableField("ip_id")
    private Integer ipId;

    @TableField("port")
    private Integer port;

    /** 占用者类型：EOP_APP / EOP_BOUND / PROTOCOL / EDGE_SYSTEM / CDN_SERVICE */
    @TableField("occupier_type")
    private IpPortOccupierType occupierType;

    /**
     * 占用者 ID，含义随 occupierType 而变：
     *   EOP_APP      → eop_app.id
     *   EOP_BOUND    → eop_bound.id
     *   EDGE_SYSTEM  → edge.id
     *   CDN_SERVICE  → cdn_service.id（将来）
     */
    @TableField("occupier_id")
    private Integer occupierId;

    @TableField("created_at")
    private Instant createdAt;

    public Integer getId() { return id; }
    public EdgeIpPortOccupationEntity setId(Integer id) { this.id = id; return this; }

    public Integer getIpId() { return ipId; }
    public EdgeIpPortOccupationEntity setIpId(Integer ipId) { this.ipId = ipId; return this; }

    public Integer getPort() { return port; }
    public EdgeIpPortOccupationEntity setPort(Integer port) { this.port = port; return this; }

    public IpPortOccupierType getOccupierType() { return occupierType; }
    public EdgeIpPortOccupationEntity setOccupierType(IpPortOccupierType occupierType) { this.occupierType = occupierType; return this; }

    public Integer getOccupierId() { return occupierId; }
    public EdgeIpPortOccupationEntity setOccupierId(Integer occupierId) { this.occupierId = occupierId; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EdgeIpPortOccupationEntity setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }
}
