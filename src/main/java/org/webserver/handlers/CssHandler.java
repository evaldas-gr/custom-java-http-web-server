package org.webserver.handlers;

import org.webserver.http.HttpMethod;
import org.webserver.http.data.HttpRequest;
import org.webserver.http.data.HttpResponse;
import org.webserver.http.data.HttpStatus;
import org.webserver.http.exceptions.HttpException;
import org.webserver.http.handlers.HttpHandler;
import org.webserver.resources.Resource;

import java.util.HashMap;
import java.util.Map;

import static org.webserver.http.data.headers.HttpContentTypes.APPLICATION_TEXT_CSS;
import static org.webserver.http.data.headers.HttpHeaders.CONTENT_TYPE;

public class CssHandler extends HttpHandler {

    private final Resource resource;
    private static final Map<String, String> headers = new HashMap<>(Map.of(CONTENT_TYPE.getValue(), APPLICATION_TEXT_CSS.getValue()));

    public CssHandler(String path, Resource resource) {
        super(path, HttpMethod.GET);
        this.resource = resource;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws HttpException {
        HttpResponse response = new HttpResponse(request.getMetadata().getProtocol(), HttpStatus.OK);
        response.addHeaders(headers);
        response.setContentData(resource.getContent());

        return response;
    }
}