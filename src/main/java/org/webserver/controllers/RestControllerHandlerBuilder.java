package org.webserver.controllers;

import org.webserver.controllers.helpers.ControllerHelper;
import org.webserver.http.data.HttpRequest;
import org.webserver.http.data.HttpResponse;
import org.webserver.http.data.HttpStatus;
import org.webserver.http.exceptions.HttpException;
import org.webserver.http.handlers.HttpHandler;

import java.util.HashMap;
import java.util.Map;

public class RestControllerHandlerBuilder<A extends IRequest, B extends IResponse> {
    private final String controllerPath;

    public RestControllerHandlerBuilder(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    public HttpHandler buildHandler(IEndpoint<A, B> endpointHandler) throws HttpException {
        return new HttpHandler(controllerPath + endpointHandler.getEndpointPathFormat(), endpointHandler.getMethod()) {
            @Override
            public HttpResponse handle(HttpRequest httpRequest) throws HttpException {
                A endpointRequest = ControllerHelper.parsePayload(httpRequest.getData(), endpointHandler.requestClass());
                B endpointResponse = endpointHandler.handle(endpointRequest);
                byte[] responseBytes = ControllerHelper.serializePayload(endpointResponse);

                Map<String, String> headers = new HashMap<>(endpointHandler.getHeaders());

                return new HttpResponse(
                        httpRequest.getMetadata().getProtocol(),
                        HttpStatus.OK,
                        headers,
                        responseBytes
                );
            }
        };
    }
}
