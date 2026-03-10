package com.ericross.dealership.domain;

import java.math.BigDecimal;

public class Honda extends Car {
    private final String color;
    private final String model;

    public Honda(String model, String color, BigDecimal price) {
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
