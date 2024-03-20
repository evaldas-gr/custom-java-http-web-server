package org.webserver.resources;

public class Resource {
    private final String fileName;
    private final byte[] content;

    public Resource(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getContent() {
        return content;
    }
}
