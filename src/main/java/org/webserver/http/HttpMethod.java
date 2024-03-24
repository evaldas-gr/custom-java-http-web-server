package org.webserver.http;

import org.javatuples.Pair;
import org.webserver.http.exceptions.errors.client.StatusBadRequestException;
import org.webserver.http.exceptions.errors.client.StatusMethodNotAllowedException;
import org.webserver.http.utils.HttpBufferReader;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.webserver.http.configs.HttpServerConfig.HTTP_METHOD_LIMIT;

public enum HttpMethod {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    CONNECT("CONNECT"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    PATCH("PATCH");

    private final byte[] bytes;

    HttpMethod(String text) {
        this.bytes = text.getBytes();
    }

    @Override
    public String toString() {
        return new String(bytes);
    }

    public boolean isEqual(HttpMethod method) {
        return method != null && method.bytes != null && Arrays.equals(bytes, method.bytes);
    }

    public static HttpMethod resolve(ByteBuffer buffer) {
        Pair<byte[], Boolean> requestMethod = HttpBufferReader.readUntilAnyOf(buffer, HTTP_METHOD_LIMIT, new byte[]{0x20});

        if (requestMethod.getValue1()) {
            throw new StatusMethodNotAllowedException();
        }

        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (Arrays.equals(httpMethod.bytes, requestMethod.getValue0()))
                return httpMethod;
        }
        throw new StatusBadRequestException("request contains unsupported HTTP method");
    }
}
