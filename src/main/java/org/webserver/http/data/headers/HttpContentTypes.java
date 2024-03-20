package org.webserver.http.data.headers;

public enum HttpContentTypes {
    APPLICATION_JSON("application/json"),
    APPLICATION_TEXT_HTML("text/html"),
    APPLICATION_TEXT_CSS("text/css"),
    APPLICATION_TEXT_JAVASCRIPT("text/javascript"),
    APPLICATION_VND_APPLE_MPEGURL("application/vnd.apple.mpegurl");

    private final String value;

    HttpContentTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
