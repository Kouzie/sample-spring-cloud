package com.sample.spring.cloud.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.cloud.customer.client.AccountClient;
import com.sample.spring.cloud.customer.dto.Account;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("contract")
// 생성된 jar 파일을 가져올 수 있도록 group, artifact 등 설정
// groupId:artifactId:versioncode:stub-qualifier:port
@AutoConfigureStubRunner(ids = "com.sample.spring.cloud:account:+:stubs:9080", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class AccountContractTest {

    @Autowired
    private AccountClient accountClient;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void verifyAccounts() throws JsonProcessingException {
        List<Account> accounts = accountClient.findCustomerAccounts(1l);
        log.info("accounts:" + objectMapper.writeValueAsString(accounts));
        Assertions.assertEquals(3, accounts.size());
    }
}
