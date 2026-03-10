package com.ericross.dealership.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
public class VehicleIntakeResponse
{
    IntakeSummaryDto summary;

    public VehicleIntakeResponse(){

        summary = new IntakeSummaryDto(0, new ArrayList<VehicleCandidateDto>(), new ArrayList<RejectedVehicleDto>());
    }
}

