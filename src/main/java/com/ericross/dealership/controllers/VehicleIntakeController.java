package com.ericross.dealership.controllers;

import com.ericross.dealership.annotations.VehicleIntakeAudit;
import com.ericross.dealership.dtos.VehicleIntakeRequest;
import com.ericross.dealership.dtos.VehicleIntakeResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ericross.dealership.service.VehicleIntakeService;

@RestController
@RequestMapping("/vehicle")
public class VehicleIntakeController {

    private final VehicleIntakeService vehicleIntakeService;

    public VehicleIntakeController(VehicleIntakeService vehicleIntakeService) {
        this.vehicleIntakeService = vehicleIntakeService;
    }

    @PostMapping("/intake")
    @VehicleIntakeAudit
    public ResponseEntity<VehicleIntakeResponse> intakeVehicles(@Valid @RequestBody VehicleIntakeRequest req) {

        return ResponseEntity.ok(vehicleIntakeService.intakeVehicles(req));
//        int total = req.getCandidates().size();
//
//        return ResponseEntity.ok(new VehicleIntakeResponse(new IntakeSummaryDto(total, new ArrayList<String>()
//                , new ArrayList<String>())));


    }

}


