package com.sample.spring.cloud.websocket.repository;

import com.sample.spring.cloud.websocket.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}