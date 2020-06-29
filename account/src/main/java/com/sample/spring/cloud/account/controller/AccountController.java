package com.sample.spring.cloud.account.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.cloud.account.dto.Order;
import com.sample.spring.cloud.account.dto.OrderStatus;
import com.sample.spring.cloud.account.dto.Product;
import com.sample.spring.cloud.account.model.Account;
import com.sample.spring.cloud.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final ObjectMapper mapper;
    private final AccountService accountService;
    private final RestTemplate template;

    @Value("${server.port:8080}")
    String port;

    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    //@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
    public Order receiveOrder(Order order) throws JsonProcessingException {
        log.info("Order received: {}", mapper.writeValueAsString(order));
        List<Account> accounts = accountService.findAllByCustomerId(order.getCustomerId());
        Account account = accounts.get(0);
        log.info("Account found: {}", mapper.writeValueAsString(account));
        Product[] products = template.postForObject("http://product-service/" + StringUtils.join(order.getProductIds(), ","), null, Product[].class);
        log.info("Products found: {}", mapper.writeValueAsString(products));
        order.setPrice(Arrays.stream(products).mapToInt(Product::getPrice).sum());
        if (order.getPrice() <= account.getBalance()) {
            order.setStatus(OrderStatus.ACCEPTED);
            account.setBalance(account.getBalance() - order.getPrice());
        } else {
            order.setStatus(OrderStatus.REJECTED);
        }
        return order;
    }

    @PostMapping
    public Account add(@RequestBody Account account) throws JsonProcessingException {
        log.info("Accounts add : {}", mapper.writeValueAsString(account));
        account = accountService.save(account);
        return account;
    }

    @PutMapping
    public Account update(@RequestBody Account account) throws JsonProcessingException {
        log.info("Accounts update : {}", mapper.writeValueAsString(account));
        return accountService.save(account);
    }

    @PutMapping("/withdraw/{id}/{amount}")
    public Account withdraw(@PathVariable("id") Long id, @PathVariable("amount") int amount) throws JsonProcessingException {
        Account account = accountService.findById(id);
        account.setBalance(account.getBalance() - amount);
        log.info("Accounts withdraw result: {}", mapper.writeValueAsString(account));
        return accountService.save(account);
    }

    @GetMapping("/{id}")
    public Account findById(@PathVariable("id") Long id) throws JsonProcessingException {
        Account account = accountService.findById(id);
        log.info("Accounts findById : {}", mapper.writeValueAsString(account));
        return account;
    }

    @GetMapping(value = "/customer/{customerId}", produces = "application/json; charset=utf-8")
    public List<Account> findByCustomerId(@PathVariable("customerId") Long customerId) throws JsonProcessingException {
        System.out.println("findByCustomerId invoke, port:" + port);
        List<Account> accounts = accountService.findAllByCustomerId(customerId);
        log.info("Accounts findByCustomerId : {}", mapper.writeValueAsString(accounts));
        return accounts;
    }

    @PostMapping("/ids")
    public List<Account> find(@RequestBody List<Long> ids) throws JsonProcessingException {
        List<Account> accounts = accountService.findAllById(ids);
        log.info("Accounts findByCustomerId : {}", mapper.writeValueAsString(accounts));

        return accounts;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Accounts delete : {}", id);
        accountService.delete(id);
    }
}
