package org.webserver.api.resource;

import org.webserver.controllers.IResponse;

public class ResourceResponse extends IResponse {
    public byte[] data;

    public ResourceResponse(byte[] data) {
        this.data = data;
    }
}
