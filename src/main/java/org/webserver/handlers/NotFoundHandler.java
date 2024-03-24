package org.webserver.handlers;

import org.webserver.http.data.HttpRequest;
import org.webserver.http.data.HttpResponse;
import org.webserver.http.data.HttpStatus;
import org.webserver.http.exceptions.HttpException;
import org.webserver.http.handlers.HttpHandler;

import java.util.HashMap;
import java.util.Map;

import static org.webserver.http.data.types.HttpContentType.APPLICATION_TEXT_HTML;
import static org.webserver.http.data.types.HttpHeader.CONTENT_TYPE;

public class NotFoundHandler extends HttpHandler {

    public NotFoundHandler() {
        super("*", null);
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws HttpException {

        return new HttpResponse(
                request.getMetadata().getProtocol(),
                HttpStatus.NOT_FOUND,
                new HashMap<>(Map.of(CONTENT_TYPE.getValue(), APPLICATION_TEXT_HTML.getValue())),
                "<html><head></head><body><h1>NOT FOUND</h1></body></html>".getBytes()
        );
    }
}
