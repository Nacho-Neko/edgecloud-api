package com.mikou.edgecloud.edge.domain.registry;

import java.util.Collection;
import java.util.Map;

/**
 * Runtime registry for edge presence (backed by Akka/Redis/etc.).
 * "Exists => online/registered" per your rule.
 */
public interface EdgeRegistry {

    boolean isRegistered(String edgeTag);

    Map<String, Boolean> batchIsRegistered(Collection<String> edgeTags);
}