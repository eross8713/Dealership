package com.ericross.dealership.service;

import com.ericross.dealership.providers.NHTSAProvider;
import com.ericross.dealership.dtos.IntakeSummaryDto;
import com.ericross.dealership.dtos.RejectedVehicleDto;
import com.ericross.dealership.dtos.VehicleCandidateDto;
import com.ericross.dealership.dtos.VehicleIntakeRequest;
import com.ericross.dealership.dtos.VehicleIntakeResponse;
import com.ericross.dealership.dtos.VehicleValidationResult;
import com.ericross.dealership.entity.CarEntity;
import org.springframework.stereotype.Service;
import com.ericross.dealership.repository.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class VehicleIntakeService {

    private final VehicleRepository repo;
    private final NHTSAProvider nhtsaProvider;
    private final Executor validationExecutor;

    public VehicleIntakeService(VehicleRepository repo, NHTSAProvider nhtsaProvider, Executor validationExecutor) {
        this.nhtsaProvider = nhtsaProvider;
        this.repo = repo;
        this.validationExecutor = validationExecutor;

    }

    private record ValidationOutcome(
            VehicleCandidateDto candidate,
            VehicleValidationResult validationResult,
            Executor validationExecutor
    ) {}



    public VehicleIntakeResponse intakeVehicles(VehicleIntakeRequest req){

        List<VehicleCandidateDto> accepted = new ArrayList<>();
        List<RejectedVehicleDto> rejected = new ArrayList<>();
        var futures = req.getCandidates().stream()
                .map(candidate -> CompletableFuture.supplyAsync(() ->
                        new ValidationOutcome(candidate, nhtsaProvider.validate(candidate), validationExecutor)
                ))
                .toList();

        var outcomes = futures.stream()
                .map(CompletableFuture::join)
                .toList();


//        VehicleIntakeResponse response = new VehicleIntakeResponse();
//        // validate each model in the list via the NHTSA API.
//        for(VehicleCandidateDto candidate : req.getCandidates()) {
//            VehicleValidationResult validationResult = nhtsaProvider.validate(candidate);
//            // persist valid candidates to the database and return summary of results (total, accepted, rejected with reasons)
//            if(validationResult.isValid()) {
//                repo.save(toCarEntity(candidate));
//                response.getSummary().getAccepted().add(candidate);
//                response.getSummary().setTotal(response.getSummary().getTotal() + 1);
//            } else {
//                // log or collect rejected candidates and reasons for response summary
//                response.getSummary().getRejected().add(candidate);
//                response.getSummary().setTotal(response.getSummary().getTotal() + 1);
//            }
//        }

        for (ValidationOutcome outcome : outcomes) {
            VehicleCandidateDto candidate = outcome.candidate();
            VehicleValidationResult validationResult = outcome.validationResult();

            if (validationResult.isValid()) {
                repo.save(toCarEntity(candidate));
                accepted.add(candidate);
            } else {
                rejected.add(toRejectedVehicleDto(candidate, validationResult));
            }
        }

        return new VehicleIntakeResponse(
                new IntakeSummaryDto(
                        req.getCandidates().size(),
                        accepted,
                        rejected
                )
        );
        // persist valid candidates to the database and return summary of results (total, accepted, rejected with reasons)

//        return response;
    }

    public CarEntity toCarEntity(VehicleCandidateDto req) {
        CarEntity carEntity = new CarEntity();
        carEntity.setModel(req.getModel());
        carEntity.setColor(req.getColor());
        carEntity.setMake(req.getMake());
        carEntity.setYear(req.getYear());
        carEntity.setSellerAskPrice(req.getSellerAskPrice());
        return carEntity;
    }

    public RejectedVehicleDto toRejectedVehicleDto(VehicleCandidateDto candidate, VehicleValidationResult validationResult) {
        return new RejectedVehicleDto(
                candidate,
                validationResult.getReason(),
                validationResult.getMessage()
        );
    }

    public List<CarEntity> getAllCars() {
        return repo.findAll();
    }




}
