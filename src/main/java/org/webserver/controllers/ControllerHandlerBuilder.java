package org.webserver.controllers;

import org.webserver.http.data.HttpRequest;
import org.webserver.http.data.HttpResponse;
import org.webserver.http.data.HttpStatus;
import org.webserver.http.exceptions.HttpException;
import org.webserver.http.handlers.HttpHandler;

import java.util.Map;

import static org.webserver.http.data.headers.HttpContentTypes.APPLICATION_JSON;
import static org.webserver.http.data.headers.HttpHeaders.CONTENT_LENGTH;
import static org.webserver.http.data.headers.HttpHeaders.CONTENT_TYPE;

public class ControllerHandlerBuilder {

    private final String controllerPath;

    public ControllerHandlerBuilder(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    public <A, B> HttpHandler buildHandler(IEndpointHandler<A, B> endpointHandler) throws HttpException {
        return new HttpHandler(controllerPath + endpointHandler.getPath(), endpointHandler.getMethod()) {
            @Override
            public HttpResponse handle(HttpRequest httpRequest) throws HttpException {
                A endpointRequest = ControllerHelper.parsePayload(httpRequest.getData(), endpointHandler.requestClass());
                B endpointResponse = endpointHandler.handle(endpointRequest);
                byte[] responseBytes = ControllerHelper.serializePayload(endpointResponse);
                HttpResponse httpResponse = new HttpResponse(
                        httpRequest.getMetadata().getProtocol(),
                        HttpStatus.OK,
                        Map.of(CONTENT_TYPE.getValue(), APPLICATION_JSON.getValue(),
                                CONTENT_LENGTH.getValue(), Integer.toString(responseBytes.length)));
                httpResponse.setContentData(responseBytes);

                return httpResponse;
            }
        };
    }
}
