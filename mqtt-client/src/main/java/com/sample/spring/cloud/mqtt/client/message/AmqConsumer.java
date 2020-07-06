//package com.sample.spring.cloud.mqtt.client.message;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.stream.annotation.StreamListener;
//import org.springframework.stereotype.Controller;
//
//@Slf4j
//@Controller
//@RequiredArgsConstructor
//public class AmqConsumer {
//
//    @StreamListener(value = AmqTopic.exchangeName)
//    public void receiveMessage(String message) {
//        log.info("message receiveMessage: {}", message);
//    }
//}
