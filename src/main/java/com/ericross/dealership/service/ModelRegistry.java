package com.ericross.dealership.service;

import java.util.Map;

public class ModelRegistry {

    private final Map<String, ModelCommand> registry;

    public ModelRegistry(Map<String, ModelCommand> registry) {
        this.registry = registry;
    }

    public ModelCommand lookup(String modelRaw) {
        if (modelRaw == null) return null;
        return registry.get(modelRaw.trim().toLowerCase());
    }
}
