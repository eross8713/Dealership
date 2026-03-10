package com.ericross.dealership.testsupport;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com/ericross/dealership/controllers",
        "com/ericross/dealership/dtos",
        "com/ericross/dealership/factory",
        "com/ericross/dealership/models",
        "com/ericross/dealership/rules",
        "com/ericross/dealership/service",
        "com/ericross/dealership/repository"
})
public class TestApplication {
}

