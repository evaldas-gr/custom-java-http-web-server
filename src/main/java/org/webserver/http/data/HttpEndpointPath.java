package org.webserver.http.data;

import org.webserver.http.exceptions.errors.client.StatusBadRequestException;
import org.webserver.http.exceptions.errors.client.StatusUriTooLongException;
import org.webserver.http.utils.HttpBufferReader;

import java.nio.ByteBuffer;

import static org.webserver.http.configs.HttpServerConfig.MAX_PATH_LENGTH;

public class HttpEndpointPath {
    public final String path;

    public HttpEndpointPath(String path) {
        if (path == null || path.isBlank() || (path.toCharArray()[0] != '/' && !path.equals("*")))
            throw new StatusBadRequestException("invalid http path");
        validatePathLength(path.length());
        this.path = path;
    }

    private void validatePathLength(int pathLength) {
        if (pathLength <= 0 || pathLength > MAX_PATH_LENGTH)
            throw new StatusUriTooLongException("http path too long");
    }

    @Override
    public String toString() {
        return path;
    }

    public boolean isMatch(HttpPath httpPath) {
        char[] p = httpPath.toString().toCharArray();
        char[] e = this.path.toCharArray();
        int j = 0;

        for (int i = 0; i < e.length; i++) {
            if (e[i] == '*') {
                if (i + 1 < e.length && e[i + 1] == '*') {
                    break;
                }
                boolean pathPartMatched = false;
                for (; j < p.length; j++) {
                    if (p[j] == '/' || j == p.length - 1) {
                        pathPartMatched = true;
                        break;
                    }
                }
                if (!pathPartMatched) return false;
                if (i == e.length - 1 && j != p.length - 1) return false;
            } else {
                if (j > p.length - 1 || e[i] != p[j]) return false;
                if (++j <= p.length - 1 && p[j] != '/' && i == e.length - 1) return false;
            }
        }

        return true;
    }
}
