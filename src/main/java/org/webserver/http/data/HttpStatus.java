package org.webserver.http.data;

public enum HttpStatus {
    OK("200", "OK"),
    CREATED("201", "Created"),
    ACCEPTED("202", "Accepted"),
    NO_CONTENT("204", "No Content"),
    MOVED_PERMANENTLY("301", "Moved Permanently"),
    FOUND("302", "Found"),
    BAD_REQUEST("400", "Bad Request"),
    UNAUTHORIZED("401", "Unauthorized"),
    FORBIDDEN("403", "Forbidden"),
    NOT_FOUND("404", "Not Found"),
    METHOD_NOT_ALLOWED("405", "Method Not Allowed"),
    LENGTH_REQUIRED("411", "Length Required"),
    PRECONDITION_FAILED("412", "Precondition Failed"),
    URI_TOO_LONG("414", "URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE("415", "Unsupported Media Type"),
    INTERNAL_ERROR("500", "Internal Server Error");

    private String code;
    private String httpCodeText;

    HttpStatus(String code, String httpCodeText) {
        this.code = code;
        this.httpCodeText = httpCodeText;
    }

    public String getCode() {
        return code;
    }

    public String getHttpCodeText() {
        return httpCodeText;
    }
}
