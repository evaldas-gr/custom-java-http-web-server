package org.webserver.http.data;

public class HttpPayload {

    private HttpMetadata metadata;
    private byte[] data;

    public HttpPayload(HttpMetadata metadata, byte[] data) {
        this.metadata = metadata;
        this.data = data;
    }

    public HttpMetadata getMetadata() {
        return this.metadata;
    }

    public byte[] getData() {
        return data;
    }
}
