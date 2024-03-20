package org.webserver.http;

import org.webserver.http.executors.HttpInboundExecutor;
import org.webserver.http.data.HttpResponse;
import org.webserver.http.logs.HttpLogger;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class HttpEventDispatcher implements Runnable {

    private final List<HttpEvent> eventsQueue = new LinkedList<>();
    private final HttpInboundExecutor httpInboundExecutor;
//    private final HttpOutboundExecutor httpOutboundExecutor = new HttpOutboundExecutor();

    public HttpEventDispatcher(HttpHandlerResolver httpHandlerResolver) {
        this.httpInboundExecutor = new HttpInboundExecutor(httpHandlerResolver);
    }

    public void dispatch(HttpServerThread serverThread, SocketChannel socket, byte[] clientData) {
        Consumer<HttpResponse> responseSender = (_httpResponse) -> {
            synchronized (eventsQueue) {
                eventsQueue.add(new HttpEvent(serverThread, socket, _httpResponse.getBytes()));
                eventsQueue.notify();
            }
        };

        httpInboundExecutor.process(socket, clientData, responseSender);
    }

    @Override
    public void run() {
        HttpLogger.log("starting", this.getClass());
        synchronized (eventsQueue) {
            while (true) {
                try {
                    eventsQueue.wait();

                    while (!eventsQueue.isEmpty()) {
                        HttpEvent event = eventsQueue.removeFirst();
                        event.serverThread.send(event.socket, event.data);
                    }
                } catch (InterruptedException e) {
                    HttpLogger.error("interrupted", this, e);
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
