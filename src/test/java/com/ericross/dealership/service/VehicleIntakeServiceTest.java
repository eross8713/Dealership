package com.ericross.dealership.service;

import com.ericross.dealership.providers.NHTSAProvider;
import com.ericross.dealership.dtos.VehicleCandidateDto;
import com.ericross.dealership.dtos.VehicleIntakeRequest;
import com.ericross.dealership.dtos.VehicleIntakeResponse;
import com.ericross.dealership.dtos.VehicleValidationResult;
import com.ericross.dealership.entity.CarEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ericross.dealership.repository.VehicleRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VehicleIntakeServiceTest {
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private NHTSAProvider nhtsaVehicleInfoProvider;
    @Mock
    private Executors validationExecutor;
    @InjectMocks
    private VehicleIntakeService vehicleIntakeService;

    // happy path test to ensure service method makes call to repository layer and
    // then maps results to response DTO correctly. Use Mockito to mock the repository and verify interactions.
//    @Test
//    void addCar_happyPath_callsRepositoryAndReturnsCreationResponse() {
//        // setup incoming request data
//        var req = new VehicleCandidateDto("Camry", new BigDecimal(320000.00), "Blue");
//        CarEntity carEntity = new CarEntity("Camry", new BigDecimal(320000.00), "Blue");
//        carEntity.setId(11111L);
//        // mock repository interaction
//        when(vehicleRepository.save(any())).thenReturn(carEntity);
//
//        CarEntity createCarResponse = vehicleIntakeService.addCars(reqs);
//        assertTrue(createCarResponse.getId() != null);
//        assertTrue(createCarResponse.getColor().equalsIgnoreCase("blue"));
//
//        assertTrue(createCarResponse.getModel().equalsIgnoreCase("camry"));
//        assertTrue(createCarResponse.getPrice().equals(new BigDecimal(320000.00)));
//
//
//    }

    // business rules validations

    @Test
    void intakeVehicles_validCandidate_isAcceptedAndPersisted() {

        var candidate = new VehicleCandidateDto(
                "Camry",
                "Black",
                "Toyota",
                 2022,
                new BigDecimal("25000.00")
        );

        var request = new VehicleIntakeRequest(List.of(candidate));
        VehicleValidationResult validationResult = new VehicleValidationResult(true, "test reason", "test message");
        when(nhtsaVehicleInfoProvider.validate(candidate))
                .thenReturn(validationResult);

        when(vehicleRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VehicleIntakeResponse response = vehicleIntakeService.intakeVehicles(request);

        assertEquals(1, response.getSummary().getAccepted().size());
        assertEquals(candidate, response.getSummary().getAccepted().get(0));
        assertEquals(0, response.getSummary().getRejected().size());
        assertEquals(1, response.getSummary().getTotal());

        verify(vehicleRepository, times(1)).save(any(CarEntity.class));
    }

    @Test
    void intakeVehicles_invalidCandidate_notPersisted() {

        var candidate = new VehicleCandidateDto(
                "Caramel",
                "Black",
                "Toyota",
                 2022,
                new BigDecimal("25000.00")
        );

        var request = new VehicleIntakeRequest(List.of(candidate));
        VehicleValidationResult validationResult = new VehicleValidationResult(false, "test reason", "test message");
        when(nhtsaVehicleInfoProvider.validate(candidate))
                .thenReturn(validationResult);


        VehicleIntakeResponse response = vehicleIntakeService.intakeVehicles(request);

        assertEquals(0, response.getSummary().getAccepted().size());
        assertEquals(candidate ,  response.getSummary().getRejected().get(0).getVehicleCandidate());
        assertEquals(1, response.getSummary().getRejected().size());
        assertEquals(1, response.getSummary().getTotal());

        verify(vehicleRepository, times(0)).save(any(CarEntity.class));
    }


    @Test
    void intakeVehicles_mixedBag_ValidAndInvalid() {

        var candidate = new VehicleCandidateDto(
                "Camry",
                "Black",
                "Toyota",
                2022,
                new BigDecimal("25000.00")
        );
        var candidate1 = new VehicleCandidateDto(
                "Caramel",
                "Black",
                "Toyota",
                2022,
                new BigDecimal("25000.00")
        );

        var request = new VehicleIntakeRequest(List.of(candidate, candidate1));
        VehicleValidationResult validationResult = new VehicleValidationResult(true, "test reason", "test message");
        VehicleValidationResult validationResult1 = new VehicleValidationResult(false, "test reason", "test message");
        when(nhtsaVehicleInfoProvider.validate(candidate))
                .thenReturn(validationResult);
        when(nhtsaVehicleInfoProvider.validate(candidate1))
                .thenReturn(validationResult1);


        VehicleIntakeResponse response = vehicleIntakeService.intakeVehicles(request);

        assertEquals(1, response.getSummary().getAccepted().size());
        assertEquals(candidate ,  response.getSummary().getAccepted().get(0));
        assertEquals(candidate1 ,  response.getSummary().getRejected().get(0).getVehicleCandidate());
        assertEquals(1, response.getSummary().getRejected().size());
        assertEquals(2, response.getSummary().getTotal());

        verify(vehicleRepository, times(1)).save(any(CarEntity.class));
    }

    @Test
    void intakeVehicles_validatesCandidatesInParallel() {

        when(vehicleRepository.save(any(CarEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

//        NHTSAProvider slowValidProvider = candidate -> {
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                throw new RuntimeException(e);
//            }
//            return new VehicleValidationResult(true, "test reason", "test message");
//        };


        NHTSAProvider slowValidProvider = mock(NHTSAProvider.class);

        when(slowValidProvider.validate(any(VehicleCandidateDto.class)))
                .thenAnswer(invocation -> {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                    return new VehicleValidationResult(true, "test reason", "test message");
                });

        VehicleIntakeService service = new VehicleIntakeService(vehicleRepository, slowValidProvider, Executors.newFixedThreadPool(4));

        var request = new VehicleIntakeRequest(List.of(
                new VehicleCandidateDto(
                        "Camry",
                        "Black",
                        "Toyota",
                        2022,
                        new BigDecimal("25000.00")
                ),
                new VehicleCandidateDto(

                        "Civic",
                        "Blue",
                        "Honda",
                        2021,

                        new BigDecimal("22000.00")
                )
        ));

        Instant start = Instant.now();

        VehicleIntakeResponse response = service.intakeVehicles(request);

        long elapsedMillis = Duration.between(start, Instant.now()).toMillis();

        assertEquals(2, response.getSummary().getAccepted().size());
        assertEquals(0, response.getSummary().getRejected().size());
        assertEquals(2, response.getSummary().getTotal());

        verify(vehicleRepository, times(2)).save(any(CarEntity.class));

        // Sequential would be roughly 600ms+.
        // Parallel should be meaningfully lower.
        System.out.println("Elapsed time for parallel validation: " + elapsedMillis + "ms");
        assertTrue(elapsedMillis < 550,
                "Expected parallel execution to finish in under 550ms, but took " + elapsedMillis + "ms");
    }

    @Test
    void intakeVehicles_invalidCandidate_returnsRejectedCandidateWithReason() {

        var candidate = new VehicleCandidateDto(
                "FakeModel",
                "Black",
                "Toyota",
                2022,
                new BigDecimal("25000.00")
        );

        var request = new VehicleIntakeRequest(List.of(candidate));

        when(nhtsaVehicleInfoProvider.validate(candidate))
                .thenReturn(new VehicleValidationResult(false, "MODEL_NOT_ALLOWED", "Model not found for make/year"));

        VehicleIntakeResponse response = vehicleIntakeService.intakeVehicles(request);

        assertEquals(0, response.getSummary().getAccepted().size());
        assertEquals(1, response.getSummary().getRejected().size());
        assertEquals(1, response.getSummary().getTotal());

        var rejected = response.getSummary().getRejected().get(0);
        assertEquals("Toyota", rejected.getVehicleCandidate().getMake());
        assertEquals("FakeModel", rejected.getVehicleCandidate().getModel());
        assertEquals("MODEL_NOT_ALLOWED", rejected.getReasonCode());
        assertEquals("Model not found for make/year", rejected.getMessage());

        verify(vehicleRepository, never()).save(any(CarEntity.class));
    }



    /*
    make an external call to edmunds api to determine if valid model
     */
    @Test
    void addCar_invalidModel_throwsException() {

    }

}
