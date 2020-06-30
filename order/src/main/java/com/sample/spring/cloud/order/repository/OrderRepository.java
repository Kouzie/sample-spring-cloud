package com.sample.spring.cloud.order.repository;

import com.sample.spring.cloud.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    int countByCustomerId(Long customerId);
}