package com.sample.spring.cloud.websocket.controller;

import com.sample.spring.cloud.websocket.model.Client;
import com.sample.spring.cloud.websocket.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WebsocketController {

    private final ClientRepository clientRepository;

    @GetMapping("/hello")
    public void websocket_sample(Model model) {
        List<Client> clientList = clientRepository.findAll();
        for (Client client : clientList) {
            log.info("websocket_sample invoked: {}", client.toString());
        }
    }
}
