package org.webserver.http.data;

import org.javatuples.Pair;
import org.webserver.http.exceptions.errors.client.StatusBadRequestException;
import org.webserver.http.exceptions.errors.client.StatusUriTooLongException;
import org.webserver.http.utils.HttpBufferReader;

import java.nio.ByteBuffer;
import java.util.Objects;

import static org.webserver.http.configs.HttpServerConfig.MAX_PATH_LENGTH;

public class HttpPath {
    private String path;

    public HttpPath(byte[] path) {
        this.path = new String(path);
    }

    @Override
    public String toString() {
        return path;
    }

    public boolean isMatch(HttpPath httpPath) {
        return this.equals(httpPath);
    }

    public static HttpPath resolve(ByteBuffer buffer) {
        Pair<byte[], Boolean> pathBytes = HttpBufferReader.readUntilAnyOf(buffer, MAX_PATH_LENGTH, new byte[]{0x20});

        if (pathBytes.getValue1()) {
            throw new StatusUriTooLongException();
        }

        return new HttpPath(pathBytes.getValue0());
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
