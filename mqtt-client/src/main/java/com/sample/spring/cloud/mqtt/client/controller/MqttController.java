package com.sample.spring.cloud.mqtt.client.controller;

import com.sample.spring.cloud.mqtt.client.message.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MqttController {

    @Autowired
    private MessageSender messageSender;

    //@StreamListener(value = Processor.INPUT, condition = "headers['receivedRoutingKey']=='.computer.part.keyboard'")
    @StreamListener(value = Processor.INPUT)
    public void receiveString(String message, MessageHeaders messageHeaders) {
        log.info("Order received: {}", message);
        log.info("messageHeaders: {}", StringUtils.join(new MessageHeaders[]{messageHeaders}));
        /*{
            amqp_receivedDeliveryMode=NON_PERSISTENT,
            amqp_receivedExchange=amq.topic,
            amqp_deliveryTag=1,
            deliveryAttempt=1,
            amqp_consumerQueue=mqtt,
            amqp_redelivered=false,
            amqp_receivedRoutingKey=.computer.part.keyboard,
            x-mqtt-publish-qos=0,
            x-mqtt-dup=false,
            id=07ccefb7-bfaf-6951-747e-ce820c1387bf,
            amqp_consumerTag=amq.ctag-m_k-6ZVxD6zr4Qnmm87FZg,
            sourceData=(Body:'[B@5482c473(byte[15])' MessageProperties [
            headers={
                    x-mqtt-publish-qos=0,
                    x-mqtt-dup=false
            },
            contentLength=0,
            receivedDeliveryMode=NON_PERSISTENT,
            redelivered=false,
            receivedExchange=amq.topic,
            receivedRoutingKey=.computer.part.keyboard,
            deliveryTag=1,
            consumerTag=amq.ctag-m_k-6ZVxD6zr4Qnmm87FZg,
            consumerQueue=mqtt]),
            contentType=application/json,
            timestamp=1594000974373
        }*/
        log.info("send result: {}", messageSender.send(message));
    }
}
