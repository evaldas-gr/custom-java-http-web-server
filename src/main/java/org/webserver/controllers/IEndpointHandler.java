package org.webserver.controllers;

import org.webserver.http.HttpMethod;

public interface IEndpointHandler<A, B> {
    B handle(A request);
    String getPath();
    HttpMethod getMethod();
    Class<A> requestClass();
}
