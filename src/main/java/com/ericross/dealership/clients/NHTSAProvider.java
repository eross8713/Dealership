package com.ericross.dealership.clients;

import com.ericross.dealership.dtos.VehicleCandidateDto;
import com.ericross.dealership.dtos.VehicleValidationResult;

public interface NHTSAProvider {

   VehicleValidationResult validate(VehicleCandidateDto candidateDto);
}
