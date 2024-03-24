package org.webserver.http.data.types;

public enum HttpContentType {
    APPLICATION_JSON("application/json"),
    APPLICATION_TEXT_HTML("text/html"),
    APPLICATION_TEXT_CSS("text/css"),
    APPLICATION_TEXT_JAVASCRIPT("text/javascript"),
    APPLICATION_VND_APPLE_MPEGURL("application/vnd.apple.mpegurl");

    private final String value;

    HttpContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
