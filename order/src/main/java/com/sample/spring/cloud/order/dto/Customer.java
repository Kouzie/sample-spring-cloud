package com.sample.spring.cloud.order.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private Long id;
    private String name;
    private CustomerType type;
    private List<Account> accounts = new ArrayList<>();
}