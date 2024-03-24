package org.webserver.http.exceptions.errors.client;

import org.webserver.http.data.HttpStatus;
import org.webserver.http.exceptions.HttpException;

public class StatusMethodNotAllowedException extends HttpException {

    public StatusMethodNotAllowedException() {
        super(HttpStatus.METHOD_NOT_ALLOWED, null);
    }

    public StatusMethodNotAllowedException(String message) {
        super(HttpStatus.METHOD_NOT_ALLOWED, message);
    }
}