package org.webserver.controllers;

import org.webserver.api.common.EmptyRequest;
import org.webserver.api.resource.ResourceResponse;
import org.webserver.http.data.HttpRequest;
import org.webserver.http.data.HttpResponse;
import org.webserver.http.data.HttpStatus;
import org.webserver.http.exceptions.HttpException;
import org.webserver.http.handlers.HttpHandler;

public class ResourceControllerHandlerBuilder {
    public static HttpHandler buildHandler(IEndpoint<EmptyRequest, ResourceResponse> endpointHandler) throws HttpException {
        return new HttpHandler(endpointHandler.getEndpointPathFormat(), endpointHandler.getMethod()) {
            @Override
            public HttpResponse handle(HttpRequest httpRequest) throws HttpException {
                ResourceResponse response = endpointHandler.handle(new EmptyRequest());

                return new HttpResponse(
                        httpRequest.getMetadata().getProtocol(),
                        HttpStatus.OK,
                        endpointHandler.getHeaders(),
                        response.data
                );
            }
        };
    }
}
