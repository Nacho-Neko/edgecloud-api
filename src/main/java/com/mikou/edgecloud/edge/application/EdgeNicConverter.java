package com.mikou.edgecloud.edge.application;

import com.mikou.edgecloud.edge.api.dto.EdgeNicDto;
import com.mikou.edgecloud.edge.api.dto.NicIpDto;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicIpEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EdgeNicConverter {

    @Mapping(target = "ips", ignore = true)
    EdgeNicDto toNicDto(EdgeNicEntity entity);

    @Mapping(target = "allocated", expression = "java(entity.getAllocatedToId() != null)")
    NicIpDto toIpDto(EdgeNicIpEntity entity);
}
