package com.ericross.dealership.factory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RulesFactoryTest {

    @Test
    void hondaStrategyRejectsToyotaModel() {
        ModelRules rules = RulesFactory.forMake("HONDA");
        assertThrows(IllegalArgumentException.class,
                () -> rules.validate("camry"));
    }

}
