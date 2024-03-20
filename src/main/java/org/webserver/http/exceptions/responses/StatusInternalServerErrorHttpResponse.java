package org.webserver.http.exceptions.responses;

import org.webserver.http.data.headers.HttpHeaders;
import org.webserver.http.data.HttpResponse;
import org.webserver.http.data.HttpStatus;

import java.util.Map;

public class StatusInternalServerErrorHttpResponse {

    private static HttpStatus status = HttpStatus.INTERNAL_ERROR;
    private static Map<String, String> headers = Map.of(
            HttpHeaders.CONNECTION.getValue(), "Closed"
    );

    public static HttpResponse getResponse(String protocol) {
        return getResponse(protocol, headers, null);
    }

    public static HttpResponse getResponse(String protocol, String message) {
        return getResponse(protocol, headers, message);
    }

    public static HttpResponse getResponse(String protocol, Map<String, String> headers) {
        return getResponse(protocol, headers, null);
    }

    public static HttpResponse getResponse(String protocol, Map<String, String> headers, String message) {
        HttpResponse httpResponse = new HttpResponse(protocol, status, headers);
        httpResponse.setContentData(message);

        return httpResponse;
    }
}
