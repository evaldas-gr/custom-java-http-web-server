package org.webserver.controllers;

import org.webserver.http.HttpMethod;

import java.util.Map;

public interface IEndpoint<A extends IRequest, B extends IResponse> {
    B handle(A request);
    String getEndpointPathFormat();
    HttpMethod getMethod();
    Class<A> requestClass();
    Map<String, String> getHeaders();
}
