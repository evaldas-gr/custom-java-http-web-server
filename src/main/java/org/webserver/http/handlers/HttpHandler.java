package org.webserver.http.handlers;

import org.webserver.http.HttpMethod;
import org.webserver.http.data.*;
import org.webserver.http.exceptions.HttpException;

public abstract class HttpHandler {
    private final HttpPath path;
    private final HttpMethod method;

    protected HttpHandler(String path, HttpMethod method) {
        this.path = new HttpPath(path);
        this.method = method;
    }

    @Override
    public String toString() {
        return "HttpHandler{" +
                "name=" + this.getClass().getSimpleName() + '\'' +
                ", path=" + path + '\'' +
                ", method=" + method +
                '}';
    }

    public HttpPath getPath() {
        return path;
    }

    /**
     * If payload is relevant for the handler return true else false.
     *
     * @param metadata
     * @return
     */
    public boolean canHandle(HttpMetadata metadata) {
        return this.method.isEqual(metadata.getMethod()) && this.path.isMatch(metadata.getPath());
    }

    public abstract HttpResponse handle(HttpRequest request) throws HttpException;
}
