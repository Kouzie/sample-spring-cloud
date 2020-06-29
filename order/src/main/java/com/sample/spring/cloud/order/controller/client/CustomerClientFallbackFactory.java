package com.sample.spring.cloud.order.controller.client;

import com.sample.spring.cloud.order.controller.dto.Customer;
import feign.hystrix.FallbackFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerClientFallbackFactory implements FallbackFactory<CustomerClient> {

    @Autowired
    private final CacheManager cacheManager;

    @Override
    public CustomerClient create(Throwable throwable) {
        return new CustomerClient() {
            @Override
            public Customer findByIdWithAccounts(Long id) {
                log.info("findByIdWithAccounts called" + throwable.getMessage());
                Cache.ValueWrapper w = cacheManager.getCache("customers").get(id);
                if (w != null) return (Customer) w.get();
                else return new Customer();
            }
        };
    }
}
