package com.sample.spring.cloud.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.cloud.order.client.AccountClient;
import com.sample.spring.cloud.order.client.CustomerClient;
import com.sample.spring.cloud.order.client.ProductClient;
import com.sample.spring.cloud.order.dto.Account;
import com.sample.spring.cloud.order.dto.Customer;
import com.sample.spring.cloud.order.message.AccountOrder;
import com.sample.spring.cloud.order.message.OrderSender;
import com.sample.spring.cloud.order.message.ProductOrder;
import com.sample.spring.cloud.order.model.Order;
import com.sample.spring.cloud.order.model.OrderStatus;
import com.sample.spring.cloud.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final ObjectMapper mapper;
    private final OrderService orderService;
    private final AccountClient accountClient;
    private final CustomerClient customerClient;
    private final OrderSender orderSender;


    // OrderSender 를 통해 order 를 메세지 브로커로 전달. account-service 가 받아 처리
    @PostMapping
    public Order process(@RequestBody Order order) throws JsonProcessingException {
        order = orderService.save(order);
        log.info("Order saved: {}", mapper.writeValueAsString(order));
        boolean isSent = orderSender.send(order);
        log.info("Order send: {}", mapper.writeValueAsString(Collections.singletonMap("isSent", isSent)));
        return order;
    }

    @StreamListener(value = ProductOrder.INPUT)
    public void receiveProductOrder(Order order) throws JsonProcessingException {
        log.info("Order receiveProductOrder: {}", mapper.writeValueAsString(order));
        order = orderService.findById(order.getId());
        if (order.getStatus() != OrderStatus.REJECTED) {
            order.setStatus(order.getStatus());
            orderService.save(order);
            log.info("Order status updated: {}", mapper.writeValueAsString(order));
        }
    }

    //    @StreamListener(value = AccountOrder.INPUT, condition = "headers['processor']=='account'")
    @StreamListener(value = AccountOrder.INPUT)
    public void receiveAccountOrder(Order order) throws JsonProcessingException {
        log.info("Order receiveAccountOrder: {}", mapper.writeValueAsString(order));
        order = orderService.findById(order.getId());
        if (order.getStatus() != OrderStatus.REJECTED) {
            order.setStatus(order.getStatus());
            orderService.save(order);
            log.info("Order status updated: {}", mapper.writeValueAsString(order));
        }
    }

    @PutMapping("/{orderId}")
    public Order accept(@PathVariable Long orderId) {
        Order order = orderService.findById(orderId);
        Account account = accountClient.withdraw(order.getAccountId(), order.getPrice());
        log.info("withdraw result:" + account.toString());
        order.setStatus(OrderStatus.DONE);
        order = orderService.save(order);
        return order;
    }

    private int priceDiscount(int price, Customer customer) {
        double discount = 0;
        switch (customer.getType()) {
            case REGULAR:
                discount += 0.05;
                break;
            case VIP:
                discount += 0.1;
                break;
            default:
                break;
        }
        int ordersNum = orderService.countByCustomerId(customer.getId());
        discount += (ordersNum * 0.01);
        return (int) (price - (price * discount));
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        Order order = orderService.findById(id);
        Customer customer = customerClient.findByIdWithAccounts(order.getAccountId());
        log.info("customer:" + customer.toString());
        return order;
    }
}
