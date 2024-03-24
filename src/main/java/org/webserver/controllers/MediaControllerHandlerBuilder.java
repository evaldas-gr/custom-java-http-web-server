package org.webserver.controllers;

import org.webserver.api.media.stream.MediaStreamRequest;
import org.webserver.api.media.stream.MediaStreamResponse;
import org.webserver.http.data.HttpRequest;
import org.webserver.http.data.HttpResponse;
import org.webserver.http.data.HttpStatus;
import org.webserver.http.exceptions.HttpException;
import org.webserver.http.handlers.HttpHandler;

public class MediaControllerHandlerBuilder {
    public static HttpHandler buildHandler(IEndpoint<MediaStreamRequest, MediaStreamResponse> endpointHandler) throws HttpException {
        return new HttpHandler(endpointHandler.getEndpointPathFormat(), endpointHandler.getMethod()) {
            @Override
            public HttpResponse handle(HttpRequest httpRequest) throws HttpException {
                MediaStreamResponse response = endpointHandler.handle(new MediaStreamRequest(httpRequest.getMetadata().getPath().toString()));

                return new HttpResponse(
                        httpRequest.getMetadata().getProtocol(),
                        HttpStatus.OK,
                        response.headers,
                        response.content
                );
            }
        };
    }
}
