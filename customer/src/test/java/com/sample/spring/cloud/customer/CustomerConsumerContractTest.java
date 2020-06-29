package com.sample.spring.cloud.customer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.cloud.customer.client.AccountClient;
import com.sample.spring.cloud.customer.dto.Account;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ExtendWith(PactConsumerTestExt.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("contract")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {
        "account-service.ribbon.listOfServers=127.0.0.1:9080"
})
@PactTestFor(providerName = "customerServiceProvider", port = "9080")
public class CustomerConsumerContractTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private AccountClient accountClient;

    @Pact(consumer = "accountClientPact")
    public RequestResponsePact callAccountClient(PactDslWithProvider builder) throws JsonProcessingException {
        log.info("callAccountClient invoked");
        List<Account> accountList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        accountList.add(Account.builder()
                .id(1l)
                .number("123")
                .balance(5000)
                .customerId(1l)
                .build());
        accountList.add(Account.builder()
                .id(2l)
                .number("124")
                .balance(5000)
                .customerId(1l)
                .build());
        accountList.add(Account.builder()
                .id(3l)
                .number("125")
                .balance(5000)
                .customerId(1l)
                .build());

        return builder
                .given("list-of-3-accounts")
                .uponReceiving("test-account-service")
                .path("/customer/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(objectMapper.writeValueAsString(accountList), ContentType.APPLICATION_JSON)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "callAccountClient")
    public void verifyAccountsPact() throws IOException {
        // timeoutInMilliseconds 기본 1초를 늘리는것을 확인, pact 가 빠르지 않다.
        List<Account> accounts = accountClient.findCustomerAccounts(1l);
        log.info(objectMapper.writeValueAsString(accounts));
        Assertions.assertEquals(3, accounts.size());
    }
}