//package com.sample.spring.cloud.mqtt.client.message;
//
//
//import org.springframework.cloud.stream.annotation.Input;
//import org.springframework.cloud.stream.annotation.Output;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.SubscribableChannel;
//
//public interface AmqTopic {
//    String exchangeName = "amq.topic";
//
//    @Input(exchangeName)
//    SubscribableChannel subscribableChannel();
//
//    @Output(exchangeName)
//    MessageChannel messageChannel();
//}