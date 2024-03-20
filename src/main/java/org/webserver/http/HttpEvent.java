package org.webserver.http;

import java.nio.channels.SocketChannel;

public class HttpEvent {
    public HttpServerThread serverThread;
    public SocketChannel socket;
    public byte[] data;

    public HttpEvent(HttpServerThread serverThread, SocketChannel socket, byte[] data) {
        this.serverThread = serverThread;
        this.socket = socket;
        this.data = data;
    }
}
