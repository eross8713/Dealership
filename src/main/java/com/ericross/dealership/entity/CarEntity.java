package com.ericross.dealership.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Entity
@Table(name = "cars")
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    Integer year;

    @Column(nullable = false)
    private BigDecimal sellerAskPrice;

    @Column
    private String condition;

    // Constructors
    public CarEntity() {}
}
