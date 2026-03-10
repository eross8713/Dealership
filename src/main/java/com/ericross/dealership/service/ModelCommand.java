package com.ericross.dealership.service;

import com.ericross.dealership.dtos.VehicleCandidateDto;
import com.ericross.dealership.domain.ShowroomState;

@FunctionalInterface
public interface ModelCommand {
    void apply(VehicleCandidateDto req, ShowroomState state);
}
