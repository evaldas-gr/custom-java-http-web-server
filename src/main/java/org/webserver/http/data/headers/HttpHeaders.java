package org.webserver.http.data.headers;

public enum HttpHeaders {
    CONTENT_TYPE("Content-type"),
    CONTENT_LENGTH("Content-length"),
    CONNECTION( "Connection");

    private final String value;

    HttpHeaders(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
