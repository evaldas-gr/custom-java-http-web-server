package org.webserver.api.media.stream;

import org.webserver.controllers.IRequest;

public class MediaStreamRequest extends IRequest {
    public String path;

    public MediaStreamRequest(String path) {
        this.path = path;
    }
}
