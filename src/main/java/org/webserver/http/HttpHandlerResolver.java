package org.webserver.http;

import org.webserver.http.data.HttpRequest;
import org.webserver.http.handlers.HttpHandler;
import org.webserver.http.logs.HttpLogger;

import java.util.ArrayList;
import java.util.List;

public class HttpHandlerResolver {
    private List<HttpHandler> httpHandlers = new ArrayList<>();
    private HttpHandler fallbackHttpHandler;

    public HttpHandlerResolver(HttpHandler fallbackHttpHandler) {
        this.fallbackHttpHandler = fallbackHttpHandler;
    }

    public HttpHandler getHandler(HttpRequest request) {
        for (HttpHandler handler : this.httpHandlers) {
            HttpLogger.log("resolving handler: " + handler.getPath().toString(), this.getClass());
            if (handler.canHandle(request.getMetadata())) return handler;
        }
        return this.fallbackHttpHandler;
    }

    public void registerHandler(HttpHandler handler) {
        if (!this.httpHandlers.contains(handler)) {
            this.httpHandlers.add(handler);
        }
    }

    public void registerHandlers(List<HttpHandler> httpHandlers) {
        httpHandlers.forEach(handler -> {
            if (!this.httpHandlers.contains(handler)) {
                this.httpHandlers.add(handler);
            }
        });
    }

    public void registerFallbackHandler(HttpHandler handler) {
        this.fallbackHttpHandler = handler;
    }
}
