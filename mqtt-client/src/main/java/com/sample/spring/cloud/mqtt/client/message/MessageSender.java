package com.sample.spring.cloud.mqtt.client.message;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSender {
    private final Source source;

    public boolean send(String message) {
        return source.output().send(MessageBuilder
                .withPayload(message)
                .setHeader("routeTo", ".receive.message")
                .build());
    }
}
