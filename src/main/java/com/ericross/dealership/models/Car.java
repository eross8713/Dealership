package com.ericross.dealership.models;

import java.math.BigDecimal;

public sealed interface Car permits HondaCar, ToyotaCar {
    BigDecimal price();
    String color();
}
