package org.webserver.controllers;

import org.webserver.http.handlers.HttpHandler;

import java.util.List;
import java.util.stream.Stream;

public interface IController {
    Stream<HttpHandler> getHandlers();
}
