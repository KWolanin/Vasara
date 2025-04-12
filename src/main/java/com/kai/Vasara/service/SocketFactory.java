package com.kai.Vasara.service;

import java.net.Socket;

public interface SocketFactory {
    Socket createSocket(String host, int port) throws Exception;
}