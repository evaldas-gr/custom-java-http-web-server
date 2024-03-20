package org.webserver.http.handlers;

import org.webserver.http.data.HttpMetadata;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class HttpHandlerList {

    private final ConcurrentLinkedQueue<HttpHandler> httpHandlers = new ConcurrentLinkedQueue<>();

    public HttpHandlerList() {}

    public boolean canHandle(HttpMetadata metadata) {
        return this.httpHandlers.stream().anyMatch(handler -> handler.canHandle(metadata));
    }

    public void addHandlers(List<HttpHandler> httpHandlers) {
        this.httpHandlers.addAll(httpHandlers);
    }

    public List<HttpHandler> getHttpHandlers() {
        return httpHandlers.stream().toList();
    }
}
