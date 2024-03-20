package org.webserver.http.exceptions.errors.client;

import org.webserver.http.data.HttpStatus;
import org.webserver.http.exceptions.HttpException;

public class StatusNotFoundException extends HttpException {

    public StatusNotFoundException() {
        super(HttpStatus.NOT_FOUND, "not found");
    }

    public StatusNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
