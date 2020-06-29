package com.sample.spring.cloud.account.service;

import com.sample.spring.cloud.account.model.Account;
import com.sample.spring.cloud.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional
    @PostConstruct
    public void init() {
        List<Account> accountList = new ArrayList<>();
        accountList.add(new Account("1234567896", 0, 0L));
        accountList.add(new Account("1234567897", 500000, 0L));
        accountList.add(new Account("1234567898", 600000, 0L));
        accountList.add(new Account("1234567892", 0, 1L));
        accountList.add(new Account("1234567890", 100000, 1L));
        accountList.add(new Account("1234567891", 200000, 1L));
        accountList.add(new Account("1234567894", 0, 2L));
        accountList.add(new Account("1234567893", 300000, 2L));
        accountList.add(new Account("1234567895", 400000, 2L));
        accountRepository.saveAll(accountList);
    }

    @Transactional
    public Account save(Account account) {
        return accountRepository.save(account);
    }
    @Transactional
    public Account withdraw(Long id, int amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(id + " account is not exist"));
        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }

    @Transactional
    public void delete(Long id) {
        accountRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(id + " account is not exist"));
    }

    @Transactional(readOnly = true)
    public List<Account> findAllByCustomerId(Long customerId) {
        return accountRepository.findAllByCustomerId(customerId);
    }

    @Transactional(readOnly = true)
    public List<Account> findAllById(List<Long> ids) {
        return accountRepository.findAllById(ids);
    }
}