package com.ericross.dealership.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RejectedVehicleDto {
    private VehicleCandidateDto vehicleCandidate;
    private String reasonCode;
    private String message;
}
