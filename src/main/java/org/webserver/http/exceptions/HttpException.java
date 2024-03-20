package org.webserver.http.exceptions;

import org.webserver.http.data.headers.HttpHeaders;
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
            HttpHeaders.CONNECTION.getValue(), "Closed"
    ));

    public HttpResponse getResponse(String protocol) {
        return getResponse(protocol, headers);
    }

    public HttpResponse getResponse(String protocol, Map<String, String> headers) {
        HttpResponse response = new HttpResponse(protocol, this.status, headers);
        if (this.getMessage() != null && !this.getMessage().isEmpty()) {
            response.setContentData(this.getMessage());
        }
        return response;
    }
}
