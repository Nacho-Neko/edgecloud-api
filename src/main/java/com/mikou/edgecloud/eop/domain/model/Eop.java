
package com.mikou.edgecloud.eop.domain.model;

import java.time.Instant;

public class Eop {
    private Integer id;
    private String eopTag;
    private Integer edgeId;
    private EopSettings settings;
    private Instant createdAt;
    private Instant updatedAt;

    public Eop() {
    }

    public Integer getId() {
        return id;
    }

    public Eop setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getEopTag() {
        return eopTag;
    }

    public Eop setEopTag(String eopTag) {
        this.eopTag = eopTag;
        return this;
    }

    public Integer getEdgeId() {
        return edgeId;
    }

    public Eop setEdgeId(Integer edgeId) {
        this.edgeId = edgeId;
        return this;
    }

    public EopSettings getSettings() {
        return settings;
    }

    public Eop setSettings(EopSettings settings) {
        this.settings = settings;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Eop setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Eop setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public boolean isBound() {
        return edgeId != null && updatedAt != null;
    }
}