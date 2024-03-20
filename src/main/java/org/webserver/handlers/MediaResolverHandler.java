package org.webserver.handlers;

import org.webserver.http.HttpMethod;
import org.webserver.http.data.HttpRequest;
import org.webserver.http.data.HttpResponse;
import org.webserver.http.data.HttpStatus;
import org.webserver.http.exceptions.HttpException;
import org.webserver.http.exceptions.errors.client.StatusNotFoundException;
import org.webserver.http.handlers.HttpHandler;
import org.webserver.media.MediaKey;
import org.webserver.services.MediaCacheService;

import java.util.List;
import java.util.Optional;

public class MediaResolverHandler extends HttpHandler {
    private final MediaCacheService mediaCacheService;
    private final List<MediaKey> keys;

    public MediaResolverHandler(String pathFormat, List<MediaKey> keys, MediaCacheService mediaCacheService) {
        super(pathFormat, HttpMethod.GET);
        this.keys = keys;
        this.mediaCacheService = mediaCacheService;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws HttpException {
        Optional<MediaKey> requestKey = keys.stream()
                .filter(key -> (key.getMediaRelativePath()).equals(request.getMetadata().getPath().toString()))
                .findFirst();

        if (requestKey.isEmpty()) {
            throw new StatusNotFoundException();
        }

        HttpResponse response = new HttpResponse(request.getMetadata().getProtocol(), HttpStatus.OK);
        response.addHeaders(requestKey.get().getHeaders());
        if (requestKey.get().getMediaRelativePath().endsWith(".ts")) {
            response.setEscapeContent(false);
        }
        response.setContentData(mediaCacheService.load(requestKey.get()).content);

        return response;
    }
}
