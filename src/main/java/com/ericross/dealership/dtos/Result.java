package com.ericross.dealership.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Result {

    @JsonProperty("Make_ID")
    private String Make_ID;

    @JsonProperty("Make_Name")
    private String Make_Name;

    @JsonProperty("Model_ID")
    private String Model_ID;

    @JsonProperty("Model_Name")
    private String Model_Name;
}
