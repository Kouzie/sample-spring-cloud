package com.sample.spring.cloud.account.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private int balance;
    private Long customerId;

    public Account(String number, int balance, long customerId) {
        this.number = number;
        this.balance = balance;
        this.customerId = customerId;
    }
}
