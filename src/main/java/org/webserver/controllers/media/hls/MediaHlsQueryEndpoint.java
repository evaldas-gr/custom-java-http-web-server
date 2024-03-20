package org.webserver.controllers.media.hls;

import org.webserver.api.media.MediaType;
import org.webserver.api.media.QueryMediaRequest;
import org.webserver.api.media.QueryMediaResponse;
import org.webserver.api.media.query.QueryDirectory;
import org.webserver.controllers.IEndpointHandler;
import org.webserver.http.HttpMethod;
import org.webserver.http.exceptions.errors.client.StatusBadRequestException;
import org.webserver.mappers.DirectoryMapper;
import org.webserver.services.media.hls.MediaHlsService;

import java.util.Optional;

public class MediaHlsQueryEndpoint implements IEndpointHandler<QueryMediaRequest, QueryMediaResponse> {
    private final String path = "/query";
    private final HttpMethod method = HttpMethod.POST;

    private final MediaHlsService mediaHlsService;

    public MediaHlsQueryEndpoint(MediaHlsService mediaHlsService) {
        this.mediaHlsService = mediaHlsService;
    }

    public QueryMediaResponse handle(QueryMediaRequest httpRequest) {
        switch (httpRequest.mediaType) {
            case MediaType.HLS -> {
                if (httpRequest.relativePath == null) {
                    return new QueryMediaResponse(DirectoryMapper.toApi(mediaHlsService.getDirectory()));
                } else {
                    Optional<QueryDirectory> foundDirectory = mediaHlsService.getDirectory(httpRequest.relativePath)
                            .map(DirectoryMapper::toApi);
                    return new QueryMediaResponse(foundDirectory.orElse(new QueryDirectory()));
                }
            }
            default -> throw new StatusBadRequestException("missing media type implementation");
        }
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public HttpMethod getMethod() {
        return this.method;
    }

    @Override
    public Class<QueryMediaRequest> requestClass() {
        return QueryMediaRequest.class;
    }
}
