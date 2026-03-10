package com.ericross.dealership.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleValidationResult {
    private final boolean valid;
    private final String reason;
    private final String message;
}
