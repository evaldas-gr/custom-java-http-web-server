package org.webserver.controllers.media.hls;

import org.webserver.controllers.IController;
import org.webserver.controllers.IEndpoint;
import org.webserver.controllers.MediaControllerHandlerBuilder;
import org.webserver.controllers.RestControllerHandlerBuilder;
import org.webserver.http.handlers.HttpHandler;
import org.webserver.media.HlsMediaLoader;
import org.webserver.services.MediaCacheService;
import org.webserver.services.media.hls.MediaHlsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MediaHlsController implements IController {
    private final String restPath = "/api/media";
    private final List<IEndpoint> restHandlers = new ArrayList<>();
    private final List<IEndpoint> mediaHandlers = new ArrayList<>();
    private final RestControllerHandlerBuilder restControllerHandlerBuilder = new RestControllerHandlerBuilder(restPath);

    public MediaHlsController(MediaHlsService mediaHlsService, MediaCacheService mediaCacheService, HlsMediaLoader hlsMediaLoader) throws IOException {
        this.restHandlers.add(new MediaHlsQueryEndpoint(mediaHlsService));
        this.mediaHandlers.add(new MediaHlsStreamEndpoint(mediaCacheService, hlsMediaLoader.generateMediaKeys()));
    }

    public Stream<HttpHandler> getHandlers() {
        return Stream.concat(
                this.restHandlers.stream().map(restControllerHandlerBuilder::buildHandler),
                this.mediaHandlers.stream().map(MediaControllerHandlerBuilder::buildHandler)
        );
    }
}
