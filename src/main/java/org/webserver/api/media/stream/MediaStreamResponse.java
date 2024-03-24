package org.webserver.api.media.stream;

import org.webserver.controllers.IResponse;

import java.util.Map;

public class MediaStreamResponse extends IResponse {
    public byte[] content;
    public Map<String, String> headers;
    public boolean escapeContent = false;

    public MediaStreamResponse(byte[] content, Map<String, String> headers) {
        this.content = content;
        this.headers = headers;
    }

    public MediaStreamResponse(byte[] content, Map<String, String> headers, boolean escapeContent) {
        this.content = content;
        this.headers = headers;
        this.escapeContent = escapeContent;
    }
}
