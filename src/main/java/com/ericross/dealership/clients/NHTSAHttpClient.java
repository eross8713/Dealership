package com.ericross.dealership.clients;

import com.ericross.dealership.dtos.NHTSAResponse;
import com.ericross.dealership.dtos.Result;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class NHTSAHttpClient implements NHTSAClient {

    private final WebClient webClient;

    public NHTSAHttpClient(WebClient nhtsaWebClient) {
        this.webClient = nhtsaWebClient;
    }

    @Override
    public List<String> getModelsForMakeAndYear(String make, Integer year) {
        Mono<NHTSAResponse> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/GetModelsForMakeYear/make/{make}/modelyear/{year}")
                        .queryParam("format", "json")
                        .build(make, year))
                .retrieve()
                .bodyToMono(NHTSAResponse.class);

        NHTSAResponse resp = response.block();
        List<String> models = new ArrayList<>();
        if (resp != null && !resp.getResults().isEmpty()) {
            for(Result result: resp.getResults()) {
                models.add(result.getModel_Name());
            }
        }
        return models;
    }
}
