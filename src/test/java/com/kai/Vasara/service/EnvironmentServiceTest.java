package com.kai.Vasara.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvironmentServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private SocketFactory socketFactory;

    private EnvironmentService environmentService;

    @BeforeEach
    void setUp() {
        environmentService = new EnvironmentService(entityManager, socketFactory);
    }

    @Test
    void isLocalhost_returnsTrue_whenSocketConnectionIsSuccessful() throws Exception {
        Socket mockSocket = mock(Socket.class);
        when(socketFactory.createSocket("127.0.0.1", 9000)).thenReturn(mockSocket);
        assertTrue(environmentService.isLocalhost());
    }

    @Test
    void isLocalhost_returnsFalse_whenSocketConnectionFails() throws Exception {
        when(socketFactory.createSocket("127.0.0.1", 9000)).thenThrow(new Exception("Connection failed"));
        assertFalse(environmentService.isLocalhost());
    }

    @Test
    void isPostgreSQL_returnsTrue_whenDatabaseIsPostgreSQL() {
        Query mockQuery = mock(Query.class);
        when(entityManager.createNativeQuery("SELECT version()")).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn("PostgreSQL 13.3");

        assertTrue(environmentService.isPostgreSQL());
    }

    @Test
    void isPostgreSQL_returnsFalse_whenDatabaseIsNotPostgreSQL() {
        Query mockQuery = mock(Query.class);
        when(entityManager.createNativeQuery("SELECT version()")).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn("MySQL 8.0");

        assertFalse(environmentService.isPostgreSQL());
    }
}