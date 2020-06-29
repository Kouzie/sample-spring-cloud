package com.sample.spring.cloud.order.controller.message;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface AccountOrder {
    String INPUT = "accountOrdersIn";
    String OUTPUT = "accountOrdersOut";

    @Input(INPUT)
    SubscribableChannel accountOrdersIn();

    @Output(OUTPUT)
    MessageChannel accountOrdersOut();
}
