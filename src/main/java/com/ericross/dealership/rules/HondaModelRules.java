package com.ericross.dealership.rules;

public final class HondaModelRules implements ModelRules {

    @Override
    public void validate(String model) {
        if (model == null) {
            throw new IllegalArgumentException("model required");
        }

        String m = model.trim().toLowerCase();

        if (!m.equals("civic")
                && !m.equals("accord")
                && !m.equals("prelude")) {
            throw new IllegalArgumentException("Invalid Honda model: " + model);
        }
    }
}
