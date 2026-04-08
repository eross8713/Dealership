package com.ericross.dealership.clients;

import java.util.List;

public interface HttpClient {
    List<String> getModelsForMakeAndYear(String make, Integer year);

}
