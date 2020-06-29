package com.sample.spring.cloud.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.cloud.order.controller.dto.CustomerType;
import com.sample.spring.cloud.order.controller.model.Order;
import com.sample.spring.cloud.order.controller.dto.Customer;
import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit5.HoverflyExtension;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.TimeUnit;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;

@Slf4j
@ExtendWith(HoverflyExtension.class)
@TestPropertySource(locations = "classpath:bootstrap-test.properties")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HoverflyTest {

    @Autowired
    TestRestTemplate template;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void init(Hoverfly hoverfly) throws JsonProcessingException {
        Customer customer = Customer.builder()
                .id(1l)
                .accounts(null)
                .name("test")
                .type(CustomerType.NEW)
                .build();
        log.info("customer:" + objectMapper.writeValueAsString(customer));

        hoverfly.simulate(dsl(
                service("customer-service:8081")
                        .andDelay(50, TimeUnit.MILLISECONDS).forAll()
                        .get("/withAccounts/1")
                        .willReturn(success("{\"id\":1,\"name\":\"test\",\"type\":\"NEW\",\"accounts\":null}", "application/json")),
                service("customer-service:9081")
                        .andDelay(200, TimeUnit.MILLISECONDS).forAll()
                        .get("/withAccounts/1")
                        .willReturn(success("{\"id\":1,\"name\":\"test\",\"type\":\"NEW\",\"accounts\":null}", "application/json"))
        ));
    }

    @Test
    public void testOrder() {
        for (int i = 0; i < 10; i++) {
            Order order = template.getForObject("/{id}", Order.class, 1);
            System.out.println(i + ":" + order.toString());
        }
    }

}
