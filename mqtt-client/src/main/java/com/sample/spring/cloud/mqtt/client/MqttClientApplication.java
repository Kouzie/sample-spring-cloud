package com.sample.spring.cloud.mqtt.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@EnableBinding(Processor.class)
@SpringBootApplication
public class MqttClientApplication {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(MqttClientApplication.class, args);
    }
}
