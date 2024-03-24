package org.webserver.resources;

public class Resource {
    private final String fileName;
    private final String relativePath;
    private final byte[] content;
    private boolean isDefaultPath = false;

    public Resource(String fileName, String relativePath, byte[] content) {
        this.fileName = fileName;
        this.relativePath = relativePath;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public boolean isDefaultPath() {
        return isDefaultPath;
    }

    public void setDefaultPath(boolean defaultPath) {
        isDefaultPath = defaultPath;
    }

    public byte[] getContent() {
        return content;
    }
}
