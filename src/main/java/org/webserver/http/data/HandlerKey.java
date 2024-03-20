package org.webserver.http.data;

import org.webserver.http.HttpMethod;

public class HandlerKey {
    private HttpMethod method;
    private HttpPath path;

    public HandlerKey(HttpPath path, HttpMethod method) {
        this.method = method;
        this.path = path;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public HttpPath getPath() {
        return this.path;
    }

    public boolean isEqual(HandlerKey key) {
        return this.method.isEqual(key.getMethod()) && this.path.isMatch(key.getPath());
    }
}
