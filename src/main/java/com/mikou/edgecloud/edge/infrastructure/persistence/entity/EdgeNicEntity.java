
package com.mikou.edgecloud.edge.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.Instant;

@TableName("edge_nic")
public class EdgeNicEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("edge_id")
    private Integer edgeId;

    @TableField("mac_address")
    private String macAddress;

    @TableField("nic_name")
    private String nicName;

    @TableField("created_at")
    private Instant createdAt;

    @TableField("updated_at")
    private Instant updatedAt;

    public Integer getId() { return id; }
    public EdgeNicEntity setId(Integer id) { this.id = id; return this; }
    public Integer getEdgeId() { return edgeId; }
    public EdgeNicEntity setEdgeId(Integer edgeId) { this.edgeId = edgeId; return this; }
    public String getMacAddress() { return macAddress; }
    public EdgeNicEntity setMacAddress(String macAddress) { this.macAddress = macAddress; return this; }
    public String getNicName() { return nicName; }
    public EdgeNicEntity setNicName(String nicName) { this.nicName = nicName; return this; }
    public Instant getCreatedAt() { return createdAt; }
    public EdgeNicEntity setCreatedAt(Instant createdAt) { this.createdAt = createdAt; return this; }
    public Instant getUpdatedAt() { return updatedAt; }
    public EdgeNicEntity setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }
}