package com.sample.spring.cloud.order.controller.message;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ProductOrder {
    String INPUT = "productOrdersIn";
    String OUTPUT = "productOrdersOut";

    @Input(INPUT)
    SubscribableChannel productOrdersIn();

    @Output(OUTPUT)
    MessageChannel productOrdersOut();
}
