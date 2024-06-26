package org.webserver.http.data;

import org.webserver.http.data.types.HttpHeader;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private String protocol;
    private HttpStatus status;
    private Map<String, String> headers = new HashMap<>();
    private byte[] data = null;

    private static final Map<String, String> SERVER_HEADERS = Map.of(HttpHeader.SERVER.getValue(), "EG");

    public HttpResponse(String protocol, HttpStatus status) {
        this.protocol = protocol;
        this.status = status;
    }

    public HttpResponse(String protocol, HttpStatus status, Map<String, String> headers) {
        this.protocol = protocol;
        this.status = status;
        this.headers = headers;
    }

    public HttpResponse(String protocol, HttpStatus status, Map<String, String> headers, byte[] data) {
        this.protocol = protocol;
        this.status = status;
        this.headers = headers;
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "protocol='" + protocol + '\'' +
                ", status=" + status + '\'' +
                ", statusCode=" + status.getCode() + '\'' +
                ", headers=" + headers + '\'' +
                ", data=" + (data == null ? "null" : "(size in bytes) " + data.length) + '\'' +
                ", totalData=" + "(size in bytes) " + getBytes().length + '\'' +
                '}';
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public byte[] getData() {
        return this.data;
    }

    public byte[] getBytes() {
        StringBuilder httpSegmentSb = new StringBuilder();
        boolean hasData = data != null;
        int contentLength = hasData ? data.length : 0;
        ByteBuffer buffer;
        int bufferSize;

        httpSegmentSb.append(this.protocol)
                .append(" ")
                .append(this.status.getCode())
                .append(" ")
                .append(this.status.getHttpCodeText())
                .append("\r\n");
        this.headers.forEach((key, value) -> httpSegmentSb.append(key).append(": ").append(value).append("\r\n"));
        SERVER_HEADERS.forEach((key, value) -> httpSegmentSb.append(key).append(": ").append(value).append("\r\n"));

        byte[] httpSegmentBytes = httpSegmentSb.append("\r\n").toString().getBytes();

        bufferSize = httpSegmentBytes.length + contentLength;
        buffer = ByteBuffer.allocate(bufferSize);
        buffer.put(httpSegmentBytes);

        if (hasData) {
            buffer.put(data);
        }

        buffer.flip();
        return buffer.array();
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setContentData(byte[] data) {
        this.data = data;
    }

    public void setContentData(String data) {
        if (data != null) {
            this.data = data.getBytes(StandardCharsets.UTF_8);
        }
    }

    public void setContentData(String data, Charset charset) {
        if (data != null) {
            this.data = data.getBytes(charset);
        }
    }
}
