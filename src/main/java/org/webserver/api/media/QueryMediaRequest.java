package org.webserver.api.media;

import org.webserver.mappers.IApiConvertable;

public class QueryMediaRequest implements IApiConvertable {
    public String relativePath;
    public String[] extensions;
    public MediaType mediaType;

    public QueryMediaRequest() {
    }
}
