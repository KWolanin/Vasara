package com.kai.Vasara.service;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.net.Socket;

@Service
@Slf4j
public class EnvironmentService {

    private final EntityManager entityManager;

    public EnvironmentService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public boolean isLocalhost() {
        try (Socket ignored = new Socket("127.0.0.1", 9000)) {
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
