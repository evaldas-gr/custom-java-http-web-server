package org.webserver.api.media;

import org.webserver.api.media.query.QueryDirectory;

public class QueryMediaResponse {
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
