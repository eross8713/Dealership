package com.ericross.dealership.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.LinkedHashMap;
import java.util.Map;

public class HondaShowroom {
    private final Map<String, Honda> inventory = new LinkedHashMap<>();

    @JsonAnyGetter
    public Map<String, Honda> asJson() { return inventory; }

    public Map<String, Honda> inventory() { return inventory; }
}
