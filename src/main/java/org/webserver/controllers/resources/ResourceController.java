package org.webserver.controllers.resources;

import org.webserver.controllers.ResourceControllerHandlerBuilder;
import org.webserver.controllers.IController;
import org.webserver.controllers.IEndpoint;
import org.webserver.http.handlers.HttpHandler;
import org.webserver.resources.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ResourceController implements IController {
    private final List<IEndpoint> endpointHandlers = new ArrayList<>();

    public ResourceController(List<Resource> resources) {
        resources.forEach(resource -> endpointHandlers.add(new GetResourceEndpoint(resource)));
    }

    public Stream<HttpHandler> getHandlers() {
        return this.endpointHandlers.stream().map(ResourceControllerHandlerBuilder::buildHandler);
    }
}
