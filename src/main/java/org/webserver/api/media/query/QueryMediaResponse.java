package org.webserver.api.media.query;

import org.webserver.api.media.query.QueryDirectory;
import org.webserver.controllers.IResponse;

public class QueryMediaResponse extends IResponse {
    private QueryDirectory directory;

    public QueryMediaResponse() {
    }

    public QueryMediaResponse(QueryDirectory directory) {
        this.directory = directory;
    }

    public QueryDirectory getDirectory() {
        return this.directory;
    }
}
