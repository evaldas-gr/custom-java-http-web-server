package org.webserver.http.exceptions.errors.client;

import org.webserver.http.data.HttpStatus;
import org.webserver.http.exceptions.HttpException;

public class StatusUriTooLongException extends HttpException {

    public StatusUriTooLongException() {
        super(HttpStatus.URI_TOO_LONG, null);
    }

    public StatusUriTooLongException(String message) {
        super(HttpStatus.URI_TOO_LONG, message);
    }
}
