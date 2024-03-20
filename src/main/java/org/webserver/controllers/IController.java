package org.webserver.controllers;

import org.webserver.http.handlers.HttpHandler;

import java.util.List;

public interface IController {
    List<HttpHandler> getHandlers();
}
