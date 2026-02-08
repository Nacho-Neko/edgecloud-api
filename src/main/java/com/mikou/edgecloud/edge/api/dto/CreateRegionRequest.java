
package com.mikou.edgecloud.edge.api.dto;

import com.mikou.edgecloud.edge.domain.enums.AreaLevel;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
public class CreateRegionRequest {
    private Integer parentId;
    private AreaLevel level;
    private String code;
    private String name;

    public Integer getParentId() {
        return parentId;
    }

    public CreateRegionRequest setParentId(Integer parentId) {
        this.parentId = parentId;
        return this;
    }

    public AreaLevel getLevel() {
        return level;
    }

    public CreateRegionRequest setLevel(AreaLevel level) {
        this.level = level;
        return this;
    }

    public String getCode() {
        return code;
    }

    public CreateRegionRequest setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreateRegionRequest setName(String name) {
        this.name = name;
        return this;
    }
}