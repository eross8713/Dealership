package com.ericross.dealership.factory;

import com.ericross.dealership.models.Car;
import com.ericross.dealership.models.HondaCar;
import com.ericross.dealership.models.ToyotaCar;

import java.math.BigDecimal;

public final class CarFactory {

    private CarFactory() {}

    public static Car create(String make, BigDecimal price, String color) {
        if (make == null) {
            throw new IllegalArgumentException("make is required");
        }

        return switch (make.trim().toUpperCase()) {
            case "HONDA" -> new HondaCar(price, color);
            case "TOYOTA" -> new ToyotaCar(price, color);
            default -> throw new IllegalArgumentException("Unsupported make: " + make);
        };
    }
}
