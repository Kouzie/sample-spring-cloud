package com.sample.spring.cloud.order.controller.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "[order]")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OrderStatus status;
    private int price;
    private Long customerId;
    private Long accountId;

    @Transient
    private List<Long> productIds;
}
