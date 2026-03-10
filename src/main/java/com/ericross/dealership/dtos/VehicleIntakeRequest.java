package com.ericross.dealership.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleIntakeRequest {
    List<VehicleCandidateDto> candidates;
}
