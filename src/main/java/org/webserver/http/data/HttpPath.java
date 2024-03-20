package org.webserver.http.data;

import org.webserver.http.exceptions.errors.client.StatusBadRequestException;
import org.webserver.http.exceptions.errors.client.StatusUriTooLongException;
import org.webserver.http.utils.HttpBufferReader;

import java.nio.ByteBuffer;
import java.util.Objects;

import static org.webserver.http.configs.HttpServerConfig.MAX_PATH_LENGTH;

public class HttpPath {
    private String path;

    public HttpPath(String path) {
        if (path == null || path.isBlank() || (path.toCharArray()[0] != '/' && !path.equals("*")))
            throw new StatusBadRequestException("invalid http path");
        validatePathLength(path.length());
        this.path = path;
    }

    public HttpPath(byte[] path) {
        validatePathLength(path.length);
        this.path = new String(path);
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
        return this.equals(httpPath);
    }

    public static HttpPath resolve(ByteBuffer buffer) {
        // TODO: do not read path after the HTTP max path limit
        byte[] pathBytes = HttpBufferReader.readUntilAnyOf(buffer, buffer.remaining(), new byte[]{0x20});

        return new HttpPath(pathBytes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpPath httpPath = (HttpPath) o;

        char[] p = httpPath.path.toCharArray();
        char[] e = this.path.toCharArray();
        int j = 0;

        for (int i = 0; i < e.length; i++) {
            if (e[i] == '*') {
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

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
