package com.mikou.edgecloud.edge.domain.registry;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link EdgeRegistry}.
 * Always returns true as per requirements.
 */
@Component
public class DefaultEdgeRegistry implements EdgeRegistry {

    @Override
    public boolean isRegistered(String edgeTag) {
        return true;
    }

    @Override
    public Map<String, Boolean> batchIsRegistered(Collection<String> edgeTags) {
        if (edgeTags == null) {
            return Map.of();
        }
        return edgeTags.stream()
                .collect(Collectors.toMap(tag -> tag, tag -> true, (v1, v2) -> v1));
    }
}
