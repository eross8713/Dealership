package com.ericross.dealership.models;

import java.math.BigDecimal;

public record ToyotaCar(BigDecimal price, String color) implements Car {


}
