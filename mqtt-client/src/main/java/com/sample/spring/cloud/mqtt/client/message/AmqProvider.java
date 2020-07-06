//package com.sample.spring.cloud.mqtt.client.message;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AmqProvider {
//
//    @Qualifier(AmqTopic.exchangeName)
//    @Autowired
//    private MessageChannel messageChannel;
//
//    public boolean send(String message) {
//        return messageChannel.send(MessageBuilder.withPayload(message).build());
//    }
//}
