package com.sample.spring.cloud.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.cloud.account.model.Account;
import com.sample.spring.cloud.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@SpringBootTest
@WebMvcTest(AccountController.class)
@ActiveProfiles("test")
// @Controller, @ControllerAdvice, @JsonComponent, Converter, GenericConverter,
// Filter, HandlerInterceptor, WebMvcConfigurer, HandlerMethodArgumentResolver 등의 빈만 등록
public class AccountControllerTest {
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mvc;

    // 가상 빈, 메서드 호출 족족 Mock proxy 클래스가 호출을 인터셉트하여 가상의 데이터를 반환한다.
    @MockBean
    AccountService accountService;

    @Test
    public void testAdd() throws Exception {
        Account account = new Account("1234567890", 5000, 1);
        when(accountService.save(any(Account.class))) // when: 특정 메서드가 호출되면 해당 데이터 반환
                .thenReturn(Account.builder()
                        .id(1l)
                        .number("1234567890")
                        .balance(5000)
                        .customerId(1l).build());
        mvc
                .perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testWithdraw() throws Exception {
        Account account = Account.builder()
                .id(1l)
                .number("1234567890")
                .balance(5000)
                .customerId(1l)
                .build();
        when(accountService.findById(1l)).thenReturn(account);
        // thenAnswer 는 input, output 을 모두 관리한다. getArgument 로 매개 변수를 받고 상호작용하여 반환값을 설정 가능하다.
        when(accountService.save(any(Account.class))).thenAnswer(new Answer<Account>() {
            @Override
            public Account answer(InvocationOnMock invocationOnMock) throws Throwable {
                Account a = invocationOnMock.getArgument(0, Account.class); // 0번째 매개 변수
                return a;
            }
        });
        mvc.perform(put("/withdraw/1/1000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance", is(4000)))
                .andDo(print());
    }
}
