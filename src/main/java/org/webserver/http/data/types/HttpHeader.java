package org.webserver.http.data.types;

public enum HttpHeader {
    SERVER("Server"),
    CONTENT_TYPE("Content-type"),
    CONTENT_LENGTH("Content-length"),
    CONNECTION( "Connection");

    private final String value;

    HttpHeader(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
