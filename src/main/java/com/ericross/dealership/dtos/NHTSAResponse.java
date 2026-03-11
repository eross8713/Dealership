package com.ericross.dealership.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NHTSAResponse {

    @JsonProperty("Count")
    private Integer Count;

    @JsonProperty("Message")
    private String Message;

    @JsonProperty("SearchCriteria")
    private String SearchCriteria;

    @JsonProperty("Results")
    private List<Result> Results;
}
