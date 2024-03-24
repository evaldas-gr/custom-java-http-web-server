package org.webserver.controllers.media.hls;

import org.webserver.api.media.stream.MediaStreamRequest;
import org.webserver.api.media.stream.MediaStreamResponse;
import org.webserver.controllers.IEndpoint;
import org.webserver.http.HttpMethod;
import org.webserver.http.exceptions.errors.client.StatusNotFoundException;
import org.webserver.media.data.MediaKey;
import org.webserver.services.MediaCacheService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MediaHlsStreamEndpoint implements IEndpoint<MediaStreamRequest, MediaStreamResponse> {
    private final String endpointFormat = "/hls/**";
    private final HttpMethod method = HttpMethod.GET;
    private final List<MediaKey> mediaKeys;
    private final MediaCacheService mediaCacheService;

    public MediaHlsStreamEndpoint(MediaCacheService mediaCacheService, List<MediaKey> mediaKeys) {
        this.mediaCacheService = mediaCacheService;
        this.mediaKeys = mediaKeys;
    }

    public MediaStreamResponse handle(MediaStreamRequest request) {
        Optional<MediaKey> mediaKeyOptional = mediaKeys.stream()
                .filter(key -> (key.getMediaRelativePath()).equals(request.path))
                .findFirst();

        if (mediaKeyOptional.isEmpty()) throw new StatusNotFoundException();

        MediaKey mediaKey = mediaKeyOptional.get();
        byte[] content = mediaCacheService.load(mediaKey).content;
        boolean escapeContent = !mediaKey.getMediaRelativePath().endsWith(".ts");

        return new MediaStreamResponse(content, mediaKey.getHeaders(), true);
    }

    @Override
    public String getEndpointPathFormat() {
        return this.endpointFormat;
    }

    @Override
    public HttpMethod getMethod() {
        return this.method;
    }

    @Override
    public Class<MediaStreamRequest> requestClass() {
        return MediaStreamRequest.class;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Map.of();
    }
}