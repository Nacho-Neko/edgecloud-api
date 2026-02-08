package com.mikou.edgecloud.edge.application;

import com.mikou.edgecloud.edge.api.dto.EdgeDetailDto;
import com.mikou.edgecloud.edge.api.dto.EdgeItemDto;
import com.mikou.edgecloud.edge.domain.registry.EdgeRegistry;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeEntity;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class EdgeConverter {

    @Autowired
    protected EdgeRegistry edgeRegistry;

    @Mapping(target = "online", source = "edgeTag", qualifiedByName = "isOnline")
    public abstract EdgeItemDto toItemDto(EdgeEntity entity);

    @Mapping(target = "online", source = "edgeTag", qualifiedByName = "isOnline")
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "capabilities", ignore = true)
    @Mapping(target = "nics", ignore = true)
    public abstract EdgeDetailDto toDetailDto(EdgeEntity entity);

    @Named("isOnline")
    protected boolean isOnline(UUID edgeTag) {
        return edgeTag != null && edgeRegistry.isRegistered(edgeTag.toString());
    }
}
