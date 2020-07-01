package com.sample.spring.cloud.account;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import com.sample.spring.cloud.account.model.Account;
import com.sample.spring.cloud.account.repository.AccountRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@Provider("customerServiceProvider")
@PactBroker(host = "127.0.0.1", port = "9292")
@ActiveProfiles("contract")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Disabled
public class AccountProviderContractTest {

    @MockBean
    private AccountRepository accountRepository;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(@NotNull PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", 9080));
    }

    @State("list-of-3-accounts")
    public void toDefaultState() {
        List<Account> accountList = new ArrayList<>();
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
        when(accountRepository.findAllByCustomerId(1l)).thenReturn(accountList);
    }

}
