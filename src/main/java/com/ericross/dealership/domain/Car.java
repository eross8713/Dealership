package com.ericross.dealership.domain;

import java.math.BigDecimal;

public class Car {
    private final BigDecimal price;

    public Car(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
