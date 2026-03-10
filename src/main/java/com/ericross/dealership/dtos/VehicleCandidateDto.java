package com.ericross.dealership.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleCandidateDto
{
    @NotBlank
    private String model;
    @NotBlank
    private String color;
    @NotBlank
    private String make;
    @NotNull
    @Min(value=4)
    @Max(value=4)
    private Integer year;
    @NotNull
    @Positive
    private BigDecimal sellerAskPrice;

}

