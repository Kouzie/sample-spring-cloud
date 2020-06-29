package com.sample.spring.cloud.order.controller.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "account-service", fallbackFactory = AccountClientFallbackFactory.class)
public interface AccountClient extends AccountFeignService {

}