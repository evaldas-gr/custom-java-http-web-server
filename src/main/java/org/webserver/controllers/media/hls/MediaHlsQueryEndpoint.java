package org.webserver.controllers.media.hls;

import org.webserver.api.media.types.MediaType;
import org.webserver.api.media.query.QueryMediaRequest;
import org.webserver.api.media.query.QueryMediaResponse;
import org.webserver.api.media.query.QueryDirectory;
import org.webserver.controllers.IEndpoint;
import org.webserver.controllers.IResponse;
import org.webserver.http.HttpMethod;
import org.webserver.http.exceptions.errors.client.StatusBadRequestException;
import org.webserver.mappers.DirectoryMapper;
import org.webserver.services.media.hls.MediaHlsService;

import java.util.Map;
import java.util.Optional;

import static org.webserver.http.data.types.HttpContentType.APPLICATION_JSON;
import static org.webserver.http.data.types.HttpHeader.CONTENT_TYPE;

public class MediaHlsQueryEndpoint implements IEndpoint<QueryMediaRequest, IResponse> {
    private final String path = "/query";
    private final HttpMethod method = HttpMethod.POST;
    private final MediaHlsService mediaHlsService;

    public MediaHlsQueryEndpoint(MediaHlsService mediaHlsService) {
        this.mediaHlsService = mediaHlsService;
    }

    public QueryMediaResponse handle(QueryMediaRequest request) {
        switch (request.mediaType) {
            case MediaType.ALL -> {
                return new QueryMediaResponse(DirectoryMapper.toApi(mediaHlsService.getDirectory()));
            }
            case MediaType.HLS -> {
                if (request.relativePath == null) {
                    return new QueryMediaResponse(DirectoryMapper.toApi(mediaHlsService.getDirectory()));
                } else {
                    Optional<QueryDirectory> foundDirectory = mediaHlsService.getDirectory(request.relativePath)
                            .map(DirectoryMapper::toApi);
                    return new QueryMediaResponse(foundDirectory.orElse(new QueryDirectory()));
                }
            }
            default -> throw new StatusBadRequestException("missing media type implementation");
        }
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
    public Class<QueryMediaRequest> requestClass() {
        return QueryMediaRequest.class;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Map.of(CONTENT_TYPE.getValue(), APPLICATION_JSON.getValue());
    }
}
