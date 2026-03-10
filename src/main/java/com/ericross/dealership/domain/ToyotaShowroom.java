package com.ericross.dealership.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.LinkedHashMap;
import java.util.Map;

public class ToyotaShowroom {
    private final Map<String, Toyota> inventory = new LinkedHashMap<>();

    @JsonAnyGetter
    public Map<String, Toyota> asJson() { return inventory; }

    public Map<String, Toyota> inventory() { return inventory; }
}
