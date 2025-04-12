package com.kai.Vasara.service;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.Socket;

@Service
@Slf4j
public class EnvironmentService {

    private final EntityManager entityManager;
    private final SocketFactory socketFactory;


    public EnvironmentService(EntityManager entityManager, SocketFactory socketFactory) {
        this.entityManager = entityManager;
        this.socketFactory = socketFactory;
    }

    public boolean isLocalhost() {
        try (Socket ignored = socketFactory.createSocket("127.0.0.1", 9000)) {
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public boolean isPostgreSQL() {
        return entityManager.createNativeQuery("SELECT version()")
                .getSingleResult().toString().toLowerCase().contains("postgresql");
    }
}
