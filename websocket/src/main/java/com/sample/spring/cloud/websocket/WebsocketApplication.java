package com.sample.spring.cloud.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;

@EnableDiscoveryClient
@SpringBootApplication
public class WebsocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
    }
}
