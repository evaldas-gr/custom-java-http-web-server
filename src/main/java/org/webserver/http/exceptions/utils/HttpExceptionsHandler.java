package org.webserver.http.exceptions.utils;

import org.webserver.http.data.HttpMetadata;
import org.webserver.http.data.HttpResponse;
import org.webserver.http.exceptions.HttpException;
import org.webserver.http.exceptions.errors.client.StatusBadRequestException;

import static org.webserver.http.configs.HttpServerConfig.HTTP_PROTOCOL_FALLBACK;

public class HttpExceptionsHandler {

    public static HttpResponse resolveResponse(HttpException httpException, HttpMetadata metadata) {
        return (metadata != null) ?
                resolveResponse(httpException, metadata.getProtocol()) :
                resolveResponse(httpException, HTTP_PROTOCOL_FALLBACK);
    }

    public static HttpResponse resolveResponse(HttpException httpException, String protocol) {
        if (protocol == null) {
            return new StatusBadRequestException("missing HTTP protocol").getResponse(protocol);
        } else {
            return httpException.getResponse(protocol);
        }
    }
}
