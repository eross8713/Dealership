package com.ericross.dealership.dtos;

import lombok.Data;

import java.util.List;

@Data
public class NHTSAResponse {

    private Integer Count;
    private String Message;
    private String SearchCriteria;
    private List<Result> Results;
}
