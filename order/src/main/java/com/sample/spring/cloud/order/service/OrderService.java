package com.sample.spring.cloud.order.service;

import com.sample.spring.cloud.order.model.Order;
import com.sample.spring.cloud.order.model.OrderStatus;
import com.sample.spring.cloud.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @PostConstruct
    private void init() {
        for (long i = 0; i < 3; i++) {
            Order order = new Order();
            order.setAccountId(i + 1);
            order.setCustomerId(i + 1);
            order.setPrice(100);
            order.setStatus(OrderStatus.values()[(int) i]);
            orderRepository.save(order);
        }
    }

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(id + " order is not exist"));
    }

    @Transactional
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public int countByCustomerId(Long customerId) {
        return orderRepository.countByCustomerId(customerId);
    }
}
