package com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mikou.edgecloud.eop.domain.enums.EopOccupierType;
import java.time.Instant;

@TableName("eop_port_occupation")
public class EopPortOccupationEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("ip_id")
    private Integer ipId;

    @TableField("port")
    private Integer port;

    /**
     * 占用者类型: EOP_APP (EopSettings), EOP_BOUND (EopService)
     */
    @TableField("occupier_type")
    private EopOccupierType occupierType;

    /**
     * 占用者ID: EopAppEntity.id 或 EopBoundEntity.id
     */
    @TableField("occupier_id")
    private Integer occupierId;

    @TableField("created_at")
    private Instant createdAt;

    public Integer getId() { return id; }
    public EopPortOccupationEntity setId(Integer id) { this.id = id; return this; }

    public Integer getIpId() { return ipId; }
    public EopPortOccupationEntity setIpId(Integer ipId) { this.ipId = ipId; return this; }

    public Integer getPort() { return port; }
    public EopPortOccupationEntity setPort(Integer port) { this.port = port; return this; }

    public EopOccupierType getOccupierType() { return occupierType; }
    public EopPortOccupationEntity setOccupierType(EopOccupierType occupierType) { this.occupierType = occupierType; return this; }

    public Integer getOccupierId() { return occupierId; }
    public EopPortOccupationEntity setOccupierId(Integer occupierId) { this.occupierId = occupierId; return this; }

    public Instant getCreatedAt() { return createdAt; }
    public EopPortOccupationEntity setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }
}
