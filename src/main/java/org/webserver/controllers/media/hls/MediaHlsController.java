package org.webserver.controllers.media.hls;

import org.webserver.controllers.ControllerHandlerBuilder;
import org.webserver.controllers.IController;
import org.webserver.controllers.IEndpointHandler;
import org.webserver.http.handlers.HttpHandler;
import org.webserver.services.media.hls.MediaHlsService;

import java.util.ArrayList;
import java.util.List;

public class MediaHlsController implements IController {
    private final String controllerPath = "/api/media";
    private final List<IEndpointHandler> endpointHandlers = new ArrayList<>();
    private final ControllerHandlerBuilder controllerHandlerBuilder = new ControllerHandlerBuilder(controllerPath);

    public MediaHlsController(MediaHlsService mediaHlsService) {
        endpointHandlers.add(new MediaHlsQueryEndpoint(mediaHlsService));
    }

    @Override
    public List<HttpHandler> getHandlers() {
        return this.endpointHandlers.stream().map(controllerHandlerBuilder::buildHandler).toList();
    }
}
