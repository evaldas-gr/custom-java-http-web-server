package org.webserver.controllers.resources;

import org.webserver.api.common.EmptyRequest;
import org.webserver.api.resource.ResourceResponse;
import org.webserver.controllers.IEndpoint;
import org.webserver.http.HttpMethod;
import org.webserver.resources.Resource;

import java.util.Map;

public class GetResourceEndpoint implements IEndpoint<EmptyRequest, ResourceResponse> {
    private final String path;
    private final HttpMethod method = HttpMethod.GET;
    private final Resource resource;

    public GetResourceEndpoint(Resource resource) {
        StringBuilder httpPathBuilder = new StringBuilder();
        if (resource.isDefaultPath()) {
            httpPathBuilder.append("/");
        } else {
            httpPathBuilder.append(resource.getRelativePath());
            if (!resource.getRelativePath().endsWith("/")) {
                httpPathBuilder.append("/");
            }
            httpPathBuilder.append(resource.getFileName());
        }
        this.path = httpPathBuilder.toString();
        this.resource = resource;
    }

    public ResourceResponse handle(EmptyRequest request) {
        return new ResourceResponse(resource.getContent());
    }

    public Map<String, String> getHeaders() {
        return ResourceHeadersResolver.resolveHeaders(resource.getFileName());
    }

    @Override
    public String getEndpointPathFormat() {
        return this.path;
    }

    @Override
    public HttpMethod getMethod() {
        return this.method;
    }

    @Override
    public Class<EmptyRequest> requestClass() {
        return EmptyRequest.class;
    }
}
