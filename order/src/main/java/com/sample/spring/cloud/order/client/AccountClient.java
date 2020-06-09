package com.sample.spring.cloud.order.client;

import com.sample.spring.cloud.order.dto.Account;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "account-service", fallbackFactory = AccountClientFallbackFactory.class)
public interface AccountClient {

    @PutMapping("/withdraw/{id}/{amount}")
    Account withdrawById(@PathVariable("id") Long id, @PathVariable("amount") Integer amount);

    @CachePut
    @GetMapping("/customer/{customerId}")
    List<Account> findByCustomer(@PathVariable("customerId") Long customerId);
}