package com.kai.Vasara.service;

import org.springframework.stereotype.Component;

import java.net.Socket;

@Component
public class DefaultSocketFactory implements SocketFactory {
    @Override
    public Socket createSocket(String host, int port) throws Exception {
        return new Socket(host, port);
    }
}