package com.sample.spring.cloud.order;

import com.sample.spring.cloud.order.model.Order;
import com.sample.spring.cloud.order.model.OrderStatus;
import com.sample.spring.cloud.order.repository.OrderRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("contract")
@SpringBootTest
@AutoConfigureStubRunner(ids = {
//        "com.sample.spring.cloud:account:+:stubs:9080",
        "com.sample.spring.cloud:customer:+:stubs:9081",
        "com.sample.spring.cloud:product:+:stubs:9083"
}, stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class OrderScenarioBase {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private OrderRepository orderRepository;


    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
        when(orderRepository.countByCustomerId(any())).thenReturn(0);
        when(orderRepository.save(any(Order.class))).thenAnswer(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) {
                Order o = invocation.getArgument(0, Order.class);
                o.setId(12345l);
                return o;
            }
        });
    }
}
