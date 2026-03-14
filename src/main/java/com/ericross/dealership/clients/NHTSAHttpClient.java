package com.ericross.dealership.clients;

import com.ericross.dealership.dtos.NHTSAResponse;
import com.ericross.dealership.dtos.Result;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class NHTSAHttpClient implements NHTSAClient {

    private final WebClient webClient;
    private final Counter nhtsaApiCallCounter;

    public NHTSAHttpClient(WebClient nhtsaWebClient, MeterRegistry meterRegistry) {
        this.webClient = nhtsaWebClient;
        this.nhtsaApiCallCounter = Counter.builder("dealearhip.nhtsa.api.calls")
                .description("Number of calls made to the NHTSA API")
                .register(meterRegistry);
    }

    @Override
    public List<String> getModelsForMakeAndYear(String make, Integer year) {
        nhtsaApiCallCounter.increment();
        Mono<NHTSAResponse> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/GetModelsForMakeYear/make/{make}/modelyear/{year}")
                        .queryParam("format", "json")
                        .build(make, year))
                .retrieve()
                .bodyToMono(NHTSAResponse.class)
                .timeout(Duration.ofSeconds(3));


        NHTSAResponse resp = response.block();
        System.out.println("Here are the results from the NHTSA API call: " + resp);

        List<String> models = new ArrayList<>();
        if (resp != null && !resp.getResults().isEmpty()) {
            for(Result result: resp.getResults()) {
                models.add(result.getModel_Name());
            }
        }
        return models;
    }
}
