package com.sample.spring.cloud.order.controller.repository;

import com.sample.spring.cloud.order.controller.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    int countByCustomerId(Long customerId);
}