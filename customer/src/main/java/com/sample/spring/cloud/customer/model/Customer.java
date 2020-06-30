package com.sample.spring.cloud.customer.model;


import com.sample.spring.cloud.customer.dto.Account;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private CustomerType type;

    @Transient
    private List<Account> accounts;

    public Customer(String name, CustomerType type) {
        this.name = name;
        this.type = type;
    }
}
