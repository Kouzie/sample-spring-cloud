package com.sample.spring.cloud.order.message;


import com.sample.spring.cloud.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductOrderSender {

    @Qualifier(ProductOrder.INPUT)
    private final SubscribableChannel productOrdersIn;
    @Qualifier(ProductOrder.OUTPUT)
    private final MessageChannel productOrdersOut;

    public boolean send(Order order) {
        return productOrdersOut.send(MessageBuilder.withPayload(order).build());
    }
}
