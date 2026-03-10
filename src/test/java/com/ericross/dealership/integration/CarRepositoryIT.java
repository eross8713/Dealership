package com.ericross.dealership.integration;

import com.ericross.dealership.entity.CarEntity;
import com.ericross.dealership.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class CarRepositoryIT {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void findAll_returnsSeededCars() {
        var cars = vehicleRepository.findAll();

        assertEquals(6, cars.size(), "Expected 6 seeded cars from V2__seed_cars.sql");

        // Optional: verify a specific seeded row exists
        CarEntity highlander = cars.stream()
                .filter(c -> "Highlander".equalsIgnoreCase(c.getModel()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Expected to find Highlander"));

        assertEquals("Toyota", highlander.getMake());
        assertEquals("Black", highlander.getColor());
    }}
