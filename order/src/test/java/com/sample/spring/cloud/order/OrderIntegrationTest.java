package com.sample.spring.cloud.order;

import com.sample.spring.cloud.order.client.AccountClient;
import com.sample.spring.cloud.order.client.CustomerClient;
import com.sample.spring.cloud.order.client.ProductClient;
import com.sample.spring.cloud.order.dto.Account;
import com.sample.spring.cloud.order.repository.OrderRepository;
import io.specto.hoverfly.junit5.HoverflyExtension;
import io.specto.hoverfly.junit5.api.HoverflySimulate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// inCaptureOrSimulateMode
@HoverflySimulate(
        source = @HoverflySimulate.Source(
                value = "src/test/resources/hoverfly/account.json",
                type = HoverflySimulate.SourceType.FILE),
        enableAutoCapture = true)
@ExtendWith(HoverflyExtension.class)
@SpringBootTest
@ActiveProfiles("dev")
@Tag("integrationTest")
@Slf4j
@Disabled
public class OrderIntegrationTest {

    @Autowired
    AccountClient accountClient;
    @Autowired
    CustomerClient customerClient;
    @Autowired
    ProductClient productClient;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void testAddAccount() {
        log.info("testAddAccount invoked");
        Account account = accountClient.add(Account.builder()
                .id(null)
                .number("123")
                .balance(5000)
                .build());
        log.info("add account:{}", account.toString());
        account = accountClient.withdraw(account.getId(), 1000);
        log.info("withdraw account:{}", account.toString());
    }
}
