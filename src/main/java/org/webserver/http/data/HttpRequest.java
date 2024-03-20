package org.webserver.http.data;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class HttpRequest  {

    private HttpMetadata metadata;
    private byte[] data;

    public HttpRequest(HttpMetadata metadata, byte[] data) {
        this.metadata = metadata;
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "metadata=" + metadata +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    public HttpMetadata getMetadata() {
        return this.metadata;
    }

    public byte[] getData() {
        return this.data;
    }

    public static HttpRequest parseRequest(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        HttpMetadata metadata = HttpMetadata.parseMetadata(buffer);
        byte[] dataBody = new byte[buffer.remaining()];
        buffer.get(dataBody);
        return new HttpRequest(metadata, dataBody);
    }
}
