
package com.mikou.edgecloud.edge.api.dto;

import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
public class CreateEdgeRequest {
    private String name;
    private Integer regionId;

    public String getName() {
        return name;
    }

    public CreateEdgeRequest setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public CreateEdgeRequest setRegionId(Integer regionId) {
        this.regionId = regionId;
        return this;
    }
}