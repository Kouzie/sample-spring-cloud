package com.sample.spring.cloud.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.cloud.order.dto.Account;
import com.sample.spring.cloud.order.dto.Customer;
import com.sample.spring.cloud.order.dto.CustomerType;
import com.sample.spring.cloud.order.dto.Product;
import com.sample.spring.cloud.order.model.Order;
import com.sample.spring.cloud.order.model.OrderStatus;
import com.sample.spring.cloud.order.repository.OrderRepository;
import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit5.HoverflyExtension;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import java.util.Collections;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.any;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.startsWith;

@Slf4j
@ExtendWith(HoverflyExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Disabled
public class OrderComponentTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    public void init(Hoverfly hoverfly) throws JsonProcessingException {
        Account account = Account.builder()
                .id(1l)
                .number("1234567890")
                .balance(5000)
                .build();

        Customer customer = Customer.builder()
                .id(1l)
                .name("test1")
                .type(CustomerType.REGULAR)
                .accounts(Collections.singletonList(account))
                .build();

        Product product = Product.builder()
                .id(1l)
                .name("Test1")
                .price(1000)
                .build();

        String accountString = objectMapper.writeValueAsString(account);
        String customerString = objectMapper.writeValueAsString(customer);
        String productListString = objectMapper.writeValueAsString(Collections.singletonList(product));

        hoverfly.simulate(dsl(
                service("account-service:8080")
                        .put(startsWith("/withdraw/"))
                        .willReturn(success(accountString, "application/json")),
                service("customer-service:8081")
                        .get("/withAccounts/1")
                        .willReturn(success(customerString, "application/json")),
                service("product-service:8083")
                        .post(any()).anyBody()
                        .willReturn(success(productListString, "application/json"))
        ));
    }

    @Test
    public void testPrepare() throws JsonProcessingException {
        Order order = Order.builder()
                .status(OrderStatus.NEW)
                .accountId(1l)
                .customerId(1l)
                .productIds(Collections.singletonList(1l))
                .build();

        order = testRestTemplate.postForObject("/", order, Order.class);
        Assertions.assertNotNull(order);
        Assertions.assertEquals(OrderStatus.ACCEPTED, order.getStatus());
        Assertions.assertEquals(940, order.getPrice());
        log.info("order: {}", objectMapper.writeValueAsString(order));
    }
}




