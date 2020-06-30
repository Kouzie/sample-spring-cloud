package com.sample.spring.cloud.customer;

import com.sample.spring.cloud.customer.client.AccountClient;
import com.sample.spring.cloud.customer.dto.Account;
import com.sample.spring.cloud.customer.model.Customer;
import com.sample.spring.cloud.customer.model.CustomerType;
import com.sample.spring.cloud.customer.repository.CustomerRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ActiveProfiles("contract")
@SpringBootTest
public class CustomerServiceBase {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private AccountClient accountClient;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
        List<Account> accountList = new ArrayList<>();
        accountList.add(Account.builder().id(1l).customerId(1l).balance(5000).number("123").build());
        accountList.add(Account.builder().id(2l).customerId(1l).balance(5000).number("124").build());
        when(accountClient.findCustomerAccounts(1l)).thenReturn(accountList);
        when(customerRepository.findById(1l)).thenReturn(Optional.of(Customer.builder()
                .id(1l)
                .name("Test1")
                .type(CustomerType.REGULAR)
                .build()));
    }

}
