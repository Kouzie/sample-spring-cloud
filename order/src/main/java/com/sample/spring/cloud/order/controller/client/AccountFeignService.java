package com.sample.spring.cloud.order.controller.client;

import com.sample.spring.cloud.order.controller.dto.Account;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * feign 클라이언트 정의용 인터페이스들을 공통 모듈에 객체로 각각의 모듈에서 공유하여
 * AccountController 에도 상속시키면 실수를 줄일 수 있다.
 */
public interface AccountFeignService {
    @PutMapping("/withdraw/{id}/{amount}")
    Account withdraw(@PathVariable("id") Long id, @PathVariable("amount") Integer amount);

    @CachePut
    @GetMapping("/customer/{customerId}")
    List<Account> findByCustomer(@PathVariable("customerId") Long customerId);

    @PostMapping
    Account add(@RequestBody Account account);
}
