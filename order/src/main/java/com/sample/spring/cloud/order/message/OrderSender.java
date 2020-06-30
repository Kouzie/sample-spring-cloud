package com.sample.spring.cloud.order.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.spring.cloud.order.model.Order;
import com.sample.spring.cloud.order.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderSender {
    private final AccountOrderSender accountOrderSender;
    private final ProductOrderSender productOrderSender;
    private final Random r = new Random();
    private final ObjectMapper objectMapper;

    public boolean send(Order order) {
        boolean result1 = accountOrderSender.send(order);
        boolean result2 = productOrderSender.send(order);
        return result1 && result2;
    }

    @Profile("!(test | dev)")
    @Bean
    @InboundChannelAdapter(value = AccountOrder.OUTPUT, poller = @Poller(fixedDelay = "3000", maxMessagesPerPoll = "1"))
    public MessageSource orderSource() {
        log.info("orderSource invoked");
        return new MessageSource() {
            @Override
            public Message receive() {
                String result = "";
                Order order = Order.builder()
                        .status(OrderStatus.NEW)
                        .accountId((long) r.nextInt(3))
                        .customerId((long) r.nextInt(3))
                        .productIds(Collections.singletonList((long) r.nextInt(3)))
                        .build();
                try {
                    result = objectMapper.writeValueAsString(order);
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                }
                return new GenericMessage(result);
            }
        };
    }
}
