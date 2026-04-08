package com.ericross.dealership.clients;

import com.ericross.dealership.dtos.NHTSAResponse;
import com.ericross.dealership.dtos.Result;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class NHTSAHttpClient implements HttpClient {

    private final WebClient webClient;
    private final Counter nhtsaApiCallCounter;
    private static final Logger log = LoggerFactory.getLogger(NHTSAHttpClient.class);

    private final Timer nhtsaApiLatencyTimer;
    private final Counter nhtsaApiFailureCounter;

    public NHTSAHttpClient(WebClient nhtsaWebClient, MeterRegistry meterRegistry) {
        this.webClient = nhtsaWebClient;
        this.nhtsaApiCallCounter = Counter.builder("dealership.nhtsa.api.calls")
                .description("Number of calls made to the NHTSA API")
                .register(meterRegistry);
        this.nhtsaApiFailureCounter = Counter.builder("dealership.nhtsa.api.failures")
                .description("Number of failed calls to the NHTSA API")
                .register(meterRegistry);

        this.nhtsaApiLatencyTimer = Timer.builder("dealership.nhtsa.api.latency")
                .description("Latency of calls made to the NHTSA API")
                .register(meterRegistry);
    }

    @Override
    public List<String> getModelsForMakeAndYear(String make, Integer year) {
        nhtsaApiCallCounter.increment();
        log.info("Calling NHTSA API for make={} year={}", make, year);
        return nhtsaApiLatencyTimer.record(() -> {
            try {
                Mono<NHTSAResponse> response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/GetModelsForMakeYear/make/{make}/modelyear/{year}")
                                .queryParam("format", "json")
                                .build(make, year))
                        .retrieve()
                        .bodyToMono(NHTSAResponse.class)
                        .timeout(Duration.ofSeconds(3));


                NHTSAResponse resp = response.block();
                int resultCount = (resp != null && resp.getResults() != null) ? resp.getResults().size() : 0;
                log.info("NHTSA API call succeeded for make={} year={} resultCount={}", make, year, resultCount);


                List<String> models = new ArrayList<>();
                if (resp != null && resp.getResults() != null &&!resp.getResults().isEmpty()) {
                    for (Result result : resp.getResults()) {
                        models.add(result.getModel_Name());
                    }
                }
                return models;
            } catch (Exception ex) {
                nhtsaApiFailureCounter.increment();
                log.error("NHTSA API call failed for make={} year={}", make, year, ex);
                throw ex;
            }
        });
    }
}
