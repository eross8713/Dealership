package com.ericross.dealership.service;

import com.ericross.dealership.clients.NHTSAHttpClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class NHTSALookupService {
    private final NHTSAHttpClient nhtsaHttpClient;

    public NHTSALookupService(NHTSAHttpClient nhtsaHttpClient) {
        this.nhtsaHttpClient = nhtsaHttpClient;
    }


    @Cacheable(
            value = "nhtsa-models",
            key = "#make + ':' + #year"
    )
    public List<String> getModelsForMakeAndYear(String make, Integer year) {
        return nhtsaHttpClient.getModelsForMakeAndYear(make, year);
    }
}
