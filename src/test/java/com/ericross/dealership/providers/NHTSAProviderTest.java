package com.ericross.dealership.providers;


import com.ericross.dealership.clients.NHTSAClient;
import com.ericross.dealership.clients.NHTSAHttpClient;
import com.ericross.dealership.dtos.VehicleCandidateDto;
import com.ericross.dealership.dtos.VehicleValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NHTSAProviderTest {

    @Mock
    private NHTSAHttpClient nhtsaHttpClient;

    @InjectMocks
    private NHTSAProvider provider;

    @Test
    void validate_whenModelExistsInNhtsaResponse_returnsValid() {
        var candidate = new VehicleCandidateDto(
                "Camry",
                "Black",
                "Toyota",
                2022,

                new BigDecimal("25000.00")
        );

        when(nhtsaHttpClient.getModelsForMakeAndYear("Toyota", 2022))
                .thenReturn(List.of("Camry", "Corolla", "Highlander"));

        VehicleValidationResult result = provider.validate(candidate);

        assertTrue(result.isValid());
    }
}
