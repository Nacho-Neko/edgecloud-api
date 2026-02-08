package com.mikou.edgecloud.edge.api.dto;

import com.mikou.edgecloud.edge.domain.enums.AreaLevel;
import java.util.List;

public class RegionTreeDto {
    private Integer id;
    private String code;
    private String name;
    private AreaLevel level;
    private List<RegionTreeDto> children;

    public Integer getId() { return id; }
    public RegionTreeDto setId(Integer id) { this.id = id; return this; }
    public String getCode() { return code; }
    public RegionTreeDto setCode(String code) { this.code = code; return this; }
    public String getName() { return name; }
    public RegionTreeDto setName(String name) { this.name = name; return this; }
    public AreaLevel getLevel() { return level; }
    public RegionTreeDto setLevel(AreaLevel level) { this.level = level; return this; }
    public List<RegionTreeDto> getChildren() { return children; }
    public RegionTreeDto setChildren(List<RegionTreeDto> children) { this.children = children; return this; }
}