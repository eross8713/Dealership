package com.ericross.dealership.factory;

import com.ericross.dealership.models.Car;
import com.ericross.dealership.models.HondaCar;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CarEntityFactoryTest {

    @Test
    void factoryCreatesHonda() {
        Car car = CarFactory.create("HONDA", new BigDecimal(20000.00) , "black");
        assertTrue(car instanceof HondaCar);
    }


}
