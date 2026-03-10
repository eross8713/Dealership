package com.ericross.dealership.clients;

import java.util.List;

public interface NHTSAClient {
    List<String> getModelsForMakeAndYear(String make, Integer year);

}
