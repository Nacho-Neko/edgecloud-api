package com.mikou.edgecloud.edge.application;

import com.mikou.edgecloud.edge.api.dto.CreateRegionRequest;
import com.mikou.edgecloud.edge.api.dto.RegionTreeDto;
import com.mikou.edgecloud.edge.domain.enums.AreaLevel;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeAreaEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeAreaMapper;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EdgeRegionService {

    private final EdgeAreaMapper regionMapper;

    public EdgeRegionService(EdgeAreaMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    @Transactional
    public Integer create(CreateRegionRequest req) {
        if (req == null || req.getName() == null || req.getName().isBlank() || req.getLevel() == null) {
            throw new IllegalArgumentException("Missing name/level");
        }

        if (req.getLevel() == AreaLevel.COUNTRY && (req.getCode() == null || req.getCode().isBlank())) {
            throw new IllegalArgumentException("Code is required for COUNTRY level");
        }

        Instant now = Instant.now();
        EdgeAreaEntity entity = new EdgeAreaEntity()
                .setParentId(req.getParentId())
                .setLevel(req.getLevel())
                .setCode(req.getCode())
                .setName(req.getName().trim())
                .setCreatedAt(now)
                .setUpdatedAt(now);

        regionMapper.insert(entity);
        return entity.getId();
    }

    public List<RegionTreeDto> getTree() {
        List<EdgeAreaEntity> all = regionMapper.selectList(null);
        Map<Integer, List<EdgeAreaEntity>> grouped = all.stream()
                .collect(Collectors.groupingBy(r -> r.getParentId() == null ? 0 : r.getParentId()));

        List<EdgeAreaEntity> countries = all.stream()
                .filter(r -> r.getLevel() == AreaLevel.COUNTRY)
                .toList();

        return buildTree(countries, grouped);
    }

    private List<RegionTreeDto> buildTree(List<EdgeAreaEntity> nodes, Map<Integer, List<EdgeAreaEntity>> grouped) {
        return nodes.stream().map(node -> {
            RegionTreeDto dto = new RegionTreeDto()
                    .setId(node.getId())
                    .setCode(node.getCode())
                    .setName(node.getName())
                    .setLevel(node.getLevel());

            List<EdgeAreaEntity> children = grouped.get(node.getId());
            if (children != null && !children.isEmpty()) {
                dto.setChildren(buildTree(children, grouped));
            }
            return dto;
        }).toList();
    }

    @Transactional
    public void delete(Integer id) {
        regionMapper.deleteById(id);
    }
}