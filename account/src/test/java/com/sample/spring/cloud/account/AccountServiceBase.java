package com.sample.spring.cloud.account;

import com.sample.spring.cloud.account.model.Account;
import com.sample.spring.cloud.account.repository.AccountRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("contract")
public abstract class AccountServiceBase {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AccountRepository accountRepository;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
        List<Account> accountList = new ArrayList<>();
        accountList.add(Account.builder()
                .id(12345l)
                .number("123")
                .balance(5000)
                .customerId(1l)
                .build());
        accountList.add(Account.builder()
                .id(22345l)
                .number("124")
                .balance(5000)
                .customerId(1l)
                .build());
        accountList.add(Account.builder()
                .id(32345l)
                .number("125")
                .balance(5000)
                .customerId(1l)
                .build());
        when(accountRepository.findAllByCustomerId(1l)).thenReturn(accountList);
    }
}