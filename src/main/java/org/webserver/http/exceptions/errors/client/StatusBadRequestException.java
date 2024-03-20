package org.webserver.http.exceptions.errors.client;

import org.webserver.http.data.HttpStatus;
import org.webserver.http.exceptions.HttpException;

public class StatusBadRequestException extends HttpException {
    public StatusBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
