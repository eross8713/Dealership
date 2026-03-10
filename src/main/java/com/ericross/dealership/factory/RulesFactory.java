package com.ericross.dealership.factory;

import com.ericross.dealership.rules.HondaModelRules;
import com.ericross.dealership.rules.ModelRules;
import com.ericross.dealership.rules.ToyotaModelRules;

public final class RulesFactory {

    private RulesFactory(){}

    public static ModelRules forMake(String make) {
        return switch (make.trim().toUpperCase()) {
            case "HONDA" -> new HondaModelRules();
            case "TOYOTA" -> new ToyotaModelRules();
            default -> throw new IllegalArgumentException("Unsupported make: " + make);
        };
    }
}

