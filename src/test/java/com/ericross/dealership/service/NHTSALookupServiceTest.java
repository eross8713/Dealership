package com.ericross.dealership.service;

import com.ericross.dealership.clients.NHTSAHttpClient;
import com.ericross.dealership.testsupport.ContainersConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@EnableCaching
@ActiveProfiles("test")
@Import(ContainersConfig.class)
public class NHTSALookupServiceTest {

    @Autowired
    private NHTSALookupService nhtsaLookupService;

    @MockBean
    private NHTSAHttpClient nhtsaClient;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void clearCaches() {
        cacheManager.getCacheNames().forEach(name -> {
            var cache = cacheManager.getCache(name);
            if (cache != null) cache.clear();
        });
    }

    @Test
    void shouldCacheModelsByMakeAndYear() {
        when(nhtsaClient.getModelsForMakeAndYear("Toyota", 2022))
                .thenReturn(List.of("Camry", "Corolla"));

        List<String> first = nhtsaLookupService.getModelsForMakeAndYear("Toyota", 2022);
        List<String> second = nhtsaLookupService.getModelsForMakeAndYear("Toyota", 2022);

        assertEquals(first, second);
        verify(nhtsaClient, times(1)).getModelsForMakeAndYear("Toyota", 2022);
    }

    @Test
    void shouldNotCacheModelsDueToDifferentYear() {
        when(nhtsaClient.getModelsForMakeAndYear("Toyota", 2022))
                .thenReturn(List.of("Camry", "Corolla"));
        when(nhtsaClient.getModelsForMakeAndYear("Toyota", 2023))
                .thenReturn(List.of("Camry", "Corolla"));

        List<String> first = nhtsaLookupService.getModelsForMakeAndYear("Toyota", 2022);
        List<String> second = nhtsaLookupService.getModelsForMakeAndYear("Toyota", 2023);

        assertEquals(first, second);
        verify(nhtsaClient, times(1)).getModelsForMakeAndYear("Toyota", 2022);
        verify(nhtsaClient, times(1)).getModelsForMakeAndYear("Toyota", 2023);
    }

    @Test
    void shouldNotCacheModelsDueToDifferentMake() {
        when(nhtsaClient.getModelsForMakeAndYear("Toyota", 2024))
                .thenReturn(List.of("Camry", "Corolla"));
        when(nhtsaClient.getModelsForMakeAndYear("Honda", 2022))
                .thenReturn(List.of("Civic", "Accord"));

        List<String> first = nhtsaLookupService.getModelsForMakeAndYear("Toyota", 2024);
        List<String> second = nhtsaLookupService.getModelsForMakeAndYear("Honda", 2022);
        List<String> third = nhtsaLookupService.getModelsForMakeAndYear("Honda", 2022);

        assertNotEquals(first, second);
        assertEquals(second, third);
        verify(nhtsaClient, times(1)).getModelsForMakeAndYear("Toyota", 2024);
        verify(nhtsaClient, times(1)).getModelsForMakeAndYear("Honda", 2022);
    }
}
