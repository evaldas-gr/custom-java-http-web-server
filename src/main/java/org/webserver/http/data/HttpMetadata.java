package org.webserver.http.data;

import org.webserver.http.HttpMethod;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class HttpMetadata {
    private HttpMethod method;
    private HttpPath path;
    private String protocol;
    private Map<String, String> attributes;

    private HttpMetadata() {}

    private HttpMetadata(HttpMethod method, HttpPath path, String protocol, Map<String, String> attributes) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "HttpMetadata{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", protocol='" + protocol + '\'' +
                ", attributes=" + attributes +
                '}';
    }

    public static HttpMetadata parseMetadata(ByteBuffer buffer) {
        HttpMethod method = HttpMethod.resolve(buffer);
        HttpPath path = HttpPath.resolve(buffer);
        String protocol = resolveProtocol(buffer);
        Map<String, String> attributes = resolveAttributes(buffer);

        return new HttpMetadata(method, path, protocol, attributes);
    }

    private static String resolveProtocol(ByteBuffer buffer) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte temp = buffer.get();
        while (temp != '\r') {
            stream.write(temp);
            temp = buffer.get();
        };
        buffer.compact();
        buffer.flip();

        return stream.toString();
    }

    private static Map<String, String> resolveAttributes(ByteBuffer buffer) {
        Map<String, String> attributes = new HashMap<>();
        final int MAX_HEADER_SIZE = 8000;
        boolean finished = false;
        boolean isSingleHeaderEnd = false;
        boolean readingValue = false;
        ByteArrayOutputStream headerStream = new ByteArrayOutputStream();
        byte[] headerKey = new byte[0];
        byte[] headerValue = new byte[0];
        byte[] chunk;
        boolean hasCarriage = false;
        int count = 0;

        while (!finished) {
            chunk = new byte[Math.min(MAX_HEADER_SIZE, buffer.remaining())];
            buffer.get(chunk);

            if (chunk.length == 0)
                break;

            for (byte b : chunk) {
                count += 1;
                if (hasCarriage && b == '\n') {
                    if (isSingleHeaderEnd) {
                        finished = true;
                        break;
                    }
                    if (readingValue) {
                        headerValue = headerStream.toByteArray();
                        headerStream.reset();
                    }
                    if (headerKey.length != 0 && headerValue.length != 0) {
                        attributes.put(new String(headerKey), new String(headerValue));
                    }
                    readingValue = false;
                    isSingleHeaderEnd = true;
                } else {
                    if (b == '\r') {
                        hasCarriage = true;
                    } else {
                        if (b == ':' && !readingValue) {
                            headerKey = headerStream.toByteArray();
                            headerStream.reset();
                            readingValue = true;
                        } else {
                            if (b != ' ')
                                headerStream.write(b);
                        }
                        hasCarriage = false;
                        isSingleHeaderEnd = false;
                    }
                }
            }
        }

        buffer.position(count);
        buffer.compact();
        buffer.flip();

        if (finished) {
            return attributes;
        } else {
            throw new RuntimeException("could not resolve request headers");
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpPath getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
