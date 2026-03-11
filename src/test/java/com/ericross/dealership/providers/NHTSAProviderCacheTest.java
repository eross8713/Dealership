package com.ericross.dealership.providers;

import com.ericross.dealership.clients.NHTSAHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@EnableCaching
@ActiveProfiles("test")
public class NHTSAProviderCacheTest {

    @Autowired
    private NHTSAProvider provider;

    @MockBean
    private NHTSAHttpClient nhtsaClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    void clearCaches() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
                .getConnection()
                .serverCommands()
                .flushAll();
    }

    @Test
    void shouldCacheModelsByMakeAndYear() {
        when(nhtsaClient.getModelsForMakeAndYear("Toyota", 2022))
                .thenReturn(List.of("Camry", "Corolla"));

        List<String> first = provider.getModelsForMakeAndYear("Toyota", 2022);
        List<String> second = provider.getModelsForMakeAndYear("Toyota", 2022);

        assertEquals(first, second);
        verify(nhtsaClient, times(1)).getModelsForMakeAndYear("Toyota", 2022);
    }

    @Test
    void shouldNotCacheModelsDueToDifferentYear() {
        when(nhtsaClient.getModelsForMakeAndYear("Toyota", 2022))
                .thenReturn(List.of("Camry", "Corolla"));
        when(nhtsaClient.getModelsForMakeAndYear("Toyota", 2023))
                .thenReturn(List.of("Camry", "Corolla"));

        List<String> first = provider.getModelsForMakeAndYear("Toyota", 2022);
        List<String> second = provider.getModelsForMakeAndYear("Toyota", 2023);

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

        List<String> first = provider.getModelsForMakeAndYear("Toyota", 2024);
        List<String> second = provider.getModelsForMakeAndYear("Honda", 2022);
        List<String> third = provider.getModelsForMakeAndYear("Honda", 2022);

        assertNotEquals(first, second);
        assertEquals(second, third);
        verify(nhtsaClient, times(1)).getModelsForMakeAndYear("Toyota", 2024);
        verify(nhtsaClient, times(1)).getModelsForMakeAndYear("Honda", 2022);
    }
}
