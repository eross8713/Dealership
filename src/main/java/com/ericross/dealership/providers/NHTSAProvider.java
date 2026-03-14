package com.ericross.dealership.providers;

import com.ericross.dealership.clients.NHTSAClient;
import com.ericross.dealership.clients.NHTSAHttpClient;
import com.ericross.dealership.dtos.VehicleCandidateDto;
import com.ericross.dealership.dtos.VehicleValidationResult;
import com.ericross.dealership.service.NHTSALookupService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NHTSAProvider {
    private final NHTSALookupService nhtsaLookupService;
   public NHTSAProvider(NHTSALookupService nhtsaLookupService) {
      this.nhtsaLookupService = nhtsaLookupService;
   }

   public VehicleValidationResult validate(VehicleCandidateDto candidate) {
        List<String> models = nhtsaLookupService.getModelsForMakeAndYear(candidate.getMake(), candidate.getYear());

        models = models.stream().map(String::toLowerCase).toList();
        if(models.contains(candidate.getModel().toLowerCase())){
            return new VehicleValidationResult(true, null, null);
        }else {
           return new VehicleValidationResult(false, "Model not found for make and year",
                   "Model " + candidate.getModel() + " not found for make " + candidate.getMake() + " and year " + candidate.getYear());
        }

   }


}
