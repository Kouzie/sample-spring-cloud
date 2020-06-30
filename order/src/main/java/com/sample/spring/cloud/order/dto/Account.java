package com.sample.spring.cloud.order.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private Long id;
    private String number;
    private int balance;
    private Long customerId;
}