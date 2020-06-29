package com.sample.spring.cloud.order.controller.message;


import com.sample.spring.cloud.order.controller.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountOrderSender {
    @Qualifier(AccountOrder.INPUT)
    private final SubscribableChannel accountOrdersIn;
    @Qualifier(AccountOrder.OUTPUT)
    private final MessageChannel accountOrdersOut;

    public boolean send(Order order) {
        return accountOrdersOut.send(MessageBuilder
                .withPayload(order).build());
    }
}
