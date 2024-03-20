package org.webserver.http.exceptions.errors.server;

import org.webserver.http.data.HttpStatus;
import org.webserver.http.exceptions.HttpException;

public class StatusInternalServerException extends HttpException {
    public StatusInternalServerException(String message) {
        super(HttpStatus.INTERNAL_ERROR, message);
    }
}
