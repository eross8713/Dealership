package com.ericross.dealership.models;

import java.math.BigDecimal;

public record HondaCar(BigDecimal price, String color) implements Car {}
