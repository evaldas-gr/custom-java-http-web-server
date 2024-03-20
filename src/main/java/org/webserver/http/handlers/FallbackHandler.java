package org.webserver.http.handlers;

import org.webserver.http.data.HttpRequest;
import org.webserver.http.data.HttpResponse;
import org.webserver.http.exceptions.errors.client.StatusNotFoundException;

public class FallbackHandler extends HttpHandler {

    public FallbackHandler() {
        super("*", null);
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return new StatusNotFoundException().getResponse(request.getMetadata().getProtocol());
    }
}
