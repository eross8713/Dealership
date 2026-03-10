package com.ericross.dealership.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IntakeSummaryDto {
    int total;
    List<VehicleCandidateDto> accepted;
    List<RejectedVehicleDto> rejected;
}
