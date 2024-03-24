package org.webserver.http.exceptions;

import org.webserver.http.data.types.HttpHeader;
import org.webserver.http.data.HttpResponse;
import org.webserver.http.data.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class HttpException extends RuntimeException {

    private final HttpStatus status;

    public HttpException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    private static Map<String, String> headers = new HashMap<>(Map.of(
            HttpHeader.CONNECTION.getValue(), "Closed"
    ));

    public HttpResponse getResponse(String protocol) {
        return getResponse(protocol, headers);
    }

    public HttpResponse getResponse(String protocol, Map<String, String> headers) {
        HttpResponse response = new HttpResponse(protocol, this.status, headers);
        if (this.getMessage() != null) {
            response.setContentData(this.getMessage().getBytes());
        }
        return response;
    }
}
