package com.sample.spring.cloud.customer;

import com.sample.spring.cloud.customer.model.Customer;
import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit5.HoverflyExtension;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled
public class CustomerControllerTest {

    @Autowired
    private TestRestTemplate template;

    @BeforeEach
    public void init(Hoverfly hoverfly) {
        hoverfly.simulate(dsl(
                service("account-service:8080")
                        .andDelay(200, TimeUnit.MILLISECONDS).forAll()
                        .get("/customer/1")
                        .willReturn(success("[{\"id\":\"1\",\"number\":\"1234567890\",\"balance\":6000}]", "application/json")),
                service("account-service:9080")
                        .andDelay(50, TimeUnit.MILLISECONDS).forAll()
                        .get("/customer/1")
                        .willReturn(success("[{\"id\":\"2\",\"number\":\"1234567891\",\"balance\":8000}]", "application/json")),
                service("account-service:10080")
                        .andDelay(10000, TimeUnit.MILLISECONDS).forAll()
                        .get("/customer/1")
                        .willReturn(success("[{\"id\":\"3\",\"number\":\"1234567890\",\"balance\":5000}]", "application/json"))
        ));
        log.info("hover:" + hoverfly.getSimulation().toString());
    }
//    @ClassRule
//    public static HoverflyRule hoverflyRule = HoverflyRule
//            .inSimulationMode(classpath("capture.json"))
//            .printSimulationData();


    @Disabled
    @Test
    public void testCustomerWithAccounts() {
        int a = 0, b = 0, d = 0;
        for (int i = 0; i < 1000; i++) {
            try {
                Customer c = template.getForObject("/withAccounts/{id}", Customer.class, 1);
                log.info("Customer: {}", c);
                if (c != null) {
                    if (c.getAccounts().get(0).getId().equals(1L))
                        a++;
                    else
                        b++;
                }
            } catch (Exception e) {
                log.error("Error connecting with service", e);
                d++;
            }
        }
        log.info("TEST RESULT: 8091={}, 9091={}, 10091={}", a, b, d);
    }
}