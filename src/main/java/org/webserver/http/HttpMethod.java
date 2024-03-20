package org.webserver.http;

import org.webserver.http.exceptions.errors.client.StatusBadRequestException;
import org.webserver.http.utils.HttpBufferReader;

import java.nio.ByteBuffer;
import java.util.Arrays;

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

    HttpMethod(final String text) {
        this.bytes = text.getBytes();
    }

    @Override
    public String toString() {
        return new String(bytes);
    }

    public boolean isEqual(HttpMethod method) {
        return method != null && method.bytes != null && Arrays.equals(bytes, method.bytes);
    }

    public static boolean isHttpMethod(HttpMethod expectedMethod, byte[] method) {
        return Arrays.equals(expectedMethod.bytes, method);
    }

    public static HttpMethod resolve(ByteBuffer buffer) {
        byte[] dataMethod = HttpBufferReader.readUntilAnyOf(buffer, Math.min(8, buffer.remaining()), new byte[]{0x20});

        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (Arrays.equals(httpMethod.bytes, dataMethod))
                return httpMethod;
        }
        throw new StatusBadRequestException("Request contains unsupported HTTP method");
    }
}
