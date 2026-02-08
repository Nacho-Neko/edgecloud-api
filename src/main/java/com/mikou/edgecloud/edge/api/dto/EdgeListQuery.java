package com.mikou.edgecloud.edge.api.dto;

import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;

@ParameterObject
public class EdgeListQuery {

    @Parameter(description = "国家ID")
    private Integer countryId;

    @Parameter(description = "区域ID")
    private Integer regionId;

    @Parameter(description = "城市ID")
    private Integer cityId;

    public Integer getCountryId() {
        return countryId;
    }

    public EdgeListQuery setCountryId(Integer countryId) {
        this.countryId = countryId;
        return this;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public EdgeListQuery setRegionId(Integer regionId) {
        this.regionId = regionId;
        return this;
    }

    public Integer getCityId() {
        return cityId;
    }

    public EdgeListQuery setCityId(Integer cityId) {
        this.cityId = cityId;
        return this;
    }
}