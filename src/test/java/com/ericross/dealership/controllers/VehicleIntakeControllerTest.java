package com.ericross.dealership.controllers;

import com.ericross.dealership.dtos.IntakeSummaryDto;
import com.ericross.dealership.dtos.VehicleCandidateDto;
import com.ericross.dealership.dtos.VehicleIntakeRequest;
import com.ericross.dealership.dtos.VehicleIntakeResponse;
import com.ericross.dealership.service.VehicleIntakeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.ericross.dealership.testsupport.TestApplication;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(classes = TestApplication.class)
@WebMvcTest(VehicleIntakeController.class)
@AutoConfigureMockMvc
public class VehicleIntakeControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private VehicleIntakeService carService;


    @Test
    void addCar_happyPath_addsSingleCarAndReturnsCorrectSummaryTotal() throws Exception {
        var candidate = new VehicleCandidateDto("Camry", "Blue", "Toyota", 2024, new BigDecimal(32000.00));
        List<VehicleCandidateDto> candidates = List.of(candidate);
        var req = new VehicleIntakeRequest(candidates);

        var summary = new IntakeSummaryDto(
                1,                  // total
                List.of(),          // accepted
                List.of()           // rejected
        );
        var mockResponse = new VehicleIntakeResponse(summary);

        when(carService.intakeVehicles(any(VehicleIntakeRequest.class)))
                .thenReturn(mockResponse);


        mockMvc.perform(post("/vehicle/intake")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary.total").value(1));

    }

    @Test
    void addCars_happyPath_addsMultipleCarsAndCorrectSummaryTotal() throws Exception {
        var candidate = new VehicleCandidateDto("Camry", "Blue", "Toyota", 2024, new BigDecimal(32000.00));
        var candidate1 = new VehicleCandidateDto("Civic", "Black", "Honda", 2016, new BigDecimal(16000.00));
        List<VehicleCandidateDto> candidates = List.of(candidate, candidate1);

        var summary = new IntakeSummaryDto(
                2,                  // total
                List.of(),          // accepted
                List.of()           // rejected
        );
        var mockResponse = new VehicleIntakeResponse(summary);

        when(carService.intakeVehicles(any(VehicleIntakeRequest.class)))
                .thenReturn(mockResponse);
        var req = new VehicleIntakeRequest(candidates);
        mockMvc.perform(post("/vehicle/intake")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary.total").value(2));

    }

    @Test
    void addCar_happyPath_addsSingleCarAndCorrectSummaryTotalWithAdditionalFields() throws Exception {
        var candidate = new VehicleCandidateDto("Camry", "Blue", "Toyota", 2024, new BigDecimal(32000.00));
        var candidate1 = new VehicleCandidateDto("Civic", "Black", "Honda", 2016, new BigDecimal(16000.00));
        List<VehicleCandidateDto> candidates = List.of(candidate, candidate1);

        var summary = new IntakeSummaryDto(
                2,                  // total
                List.of(),          // accepted
                List.of()           // rejected
        );
        var mockResponse = new VehicleIntakeResponse(summary);

        when(carService.intakeVehicles(any(VehicleIntakeRequest.class)))
                .thenReturn(mockResponse);
        var req = new VehicleIntakeRequest(candidates);
        mockMvc.perform(post("/vehicle/intake")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary.total").value(2))
                .andExpect(jsonPath("$.summary.accepted").isArray())
                .andExpect(jsonPath("$.summary.rejected").isArray());

    }
}
