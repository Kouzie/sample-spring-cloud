package com.sample.spring.cloud.customer.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sample.spring.cloud.customer.dto.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountClient {
    private final RestTemplate template;
    private final CacheManager cacheManager;

    // 서킷 브레이커와 연결된 프록시 객체로 둘러싼다.
    @HystrixCommand(
            commandKey = "customer-service.findCustomerAccounts",
            fallbackMethod = "findCustomerAccountsFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500"), //실패시 500 ms간 호출하지 않음, 기본값 1000
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), // 서킷 장애를 지정할 최소 호출 실패 수, 기본값 20
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "30"), // 최소 오류 비율. 기본값 50%
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"), // 서킷이 열린후 다시 제공할때 까지 모니터링 time, 기본값 10000
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"), // response timeout, 기본 1초
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000") // 통계의 롤링 간격
            }
    )
    @CachePut("accounts")
    public List<Account> findCustomerAccounts(Long id) {
        Account[] accounts = template.getForObject("http://account-service/customer/{customerId}", Account[].class, id);
        return Arrays.stream(accounts).collect(Collectors.toList());
    }

    public List<Account> findCustomerAccountsFallback(Long id, Throwable exception) {
        log.info("findCustomerAccountsFallback called, error:" + exception.getMessage());
//        return new ArrayList<>();
        ValueWrapper w = cacheManager.getCache("accounts").get(id);
        if (w != null) {
            log.info("call cacheManager");
            return (List<Account>) w.get();
        } else {
            return new ArrayList<>();
        }
    }
}
