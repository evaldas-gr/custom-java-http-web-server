package org.webserver.filesystem;

public class File implements Cloneable {
    private final String fileName;

    public File(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public File clone() throws CloneNotSupportedException {
        return (File) super.clone();
    }
}
