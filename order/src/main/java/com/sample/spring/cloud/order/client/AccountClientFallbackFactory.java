package com.sample.spring.cloud.order.client;

import com.sample.spring.cloud.order.dto.Account;
import feign.hystrix.FallbackFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountClientFallbackFactory implements FallbackFactory<AccountClient> {

    private final CacheManager cacheManager;

    @Override
    public AccountClient create(Throwable throwable) {
        return new AccountClient() {
            @Override
            public Account withdraw(Long id, Integer amount) {
                log.info("withdrawById called" + throwable.getMessage());
                return new Account();
            }

            @Override
            public List<Account> findByCustomer(Long customerId) {
                log.info("findByCustomer called" + throwable.getMessage());
                Cache.ValueWrapper w = cacheManager.getCache("accounts").get(customerId);
                if (w != null) {
                    return (List<Account>) w.get();
                } else {
                    return new ArrayList<Account>();
                }
            }

            @Override
            public Account add(Account account) {
                log.info("add called, account: {}, message: {}", account.toString(), throwable.getMessage());
                return null;
            }
        };
    }
}
