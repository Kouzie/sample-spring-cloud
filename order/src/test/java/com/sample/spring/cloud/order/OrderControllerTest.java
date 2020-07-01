package com.sample.spring.cloud.order;

import com.sample.spring.cloud.order.client.AccountClient;
import com.sample.spring.cloud.order.client.CustomerClient;
import com.sample.spring.cloud.order.client.ProductClient;
import com.sample.spring.cloud.order.controller.OrderController;
import com.sample.spring.cloud.order.dto.Account;
import com.sample.spring.cloud.order.model.Order;
import com.sample.spring.cloud.order.model.OrderStatus;
import com.sample.spring.cloud.order.service.OrderService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
// @Controller, @ControllerAdvice, @JsonComponent, Converter, GenericConverter,
// Filter, HandlerInterceptor, WebMvcConfigurer, HandlerMethodArgumentResolver 등의 빈만 등록
@Disabled
public class OrderControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    OrderService orderService;
    @MockBean
    ProductClient productClient;
    @MockBean
    AccountClient accountClient;
    @MockBean
    CustomerClient customerClient;

    @Test
    public void testAccept() throws Exception {
        Order order = Order.builder()
                .id(1l)
                .status(OrderStatus.DONE)
                .price(2000)
                .customerId(1l)
                .accountId(1l)
                .productIds(null)
                .build();
        when(orderService.findById(1l)).thenReturn(order);
        when(accountClient.withdraw(order.getAccountId(), order.getPrice())).thenReturn(Account.builder()
                .id(1l)
                .number("123")
                .balance(0)
                .build());
        when(orderService.save(Mockito.any(Order.class))).thenAnswer(new Answer<Order>() {
            @Override
            public Order answer(InvocationOnMock invocation) throws Throwable {
                Order o = invocation.getArgument(0, Order.class);
                return o;
            }
        });
        mvc.perform(put("/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("DONE")))
                .andDo(print());
    }
}
