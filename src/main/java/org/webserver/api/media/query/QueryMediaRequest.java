package org.webserver.api.media.query;

import org.webserver.api.media.types.MediaType;
import org.webserver.controllers.IRequest;
import org.webserver.mappers.IApiConvertable;

public class QueryMediaRequest extends IRequest implements IApiConvertable {
    public String relativePath;
    public String[] extensions;
    public MediaType mediaType;

    public QueryMediaRequest() {
    }
}
