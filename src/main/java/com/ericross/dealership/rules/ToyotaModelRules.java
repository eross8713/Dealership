package com.ericross.dealership.rules;

public final class ToyotaModelRules implements ModelRules {

    @Override
    public void validate(String model) {
        if (model == null) {
            throw new IllegalArgumentException("model required");
        }

        String m = model.trim().toLowerCase();

        if (!m.equals("camry")
                && !m.equals("corolla")
                && !m.equals("highlander")) {
            throw new IllegalArgumentException("Invalid Toyota model: " + model);
        }
    }
}
