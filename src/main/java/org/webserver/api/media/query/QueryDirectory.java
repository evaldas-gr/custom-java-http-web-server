package org.webserver.api.media.query;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class QueryDirectory {
    private String relativePath;
    private List<QueryDirectory> dirs;
    private List<String> files;

    public QueryDirectory() {
    }

    public QueryDirectory(String relativePath, List<QueryDirectory> dirs, List<String> files) {
        this.relativePath = relativePath;
        this.dirs = dirs;
        this.files = files;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public List<QueryDirectory> getDirs() {
        return dirs;
    }

    public List<String> getFiles() {
        return files;
    }
}
