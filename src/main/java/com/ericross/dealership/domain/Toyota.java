package com.ericross.dealership.domain;

import java.math.BigDecimal;

public class Toyota extends Car {
    private final String color;
    private final String model;

    public Toyota(String model, String color, BigDecimal price) {
        super(price);
        this.color = color;
        this.model = model;
    }

    public String getColor() {
        return color;
    }
    public String getModel() {
        return model;
    }
}
