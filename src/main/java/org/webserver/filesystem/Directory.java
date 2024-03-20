package org.webserver.filesystem;

import java.util.ArrayList;
import java.util.List;

public class Directory {
    private final String directoryName;
    private final String directoryPath;
    List<Directory> innerDirectories;
    List<File> files;

    public Directory(String directoryName, String directoryPath) {
        this.directoryName = directoryName;
        this.directoryPath = directoryPath;
        this.innerDirectories = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    public void addDirectory(Directory directory) {
        this.innerDirectories.add(directory);
    }

    public void addFile(File file) {
        this.files.add(file);
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public List<Directory> getInnerDirectories() {
        return innerDirectories;
    }

    public List<File> getFiles() {
        return files;
    }

    public Directory filterExtensions(List<String> extensions) {
        Directory filteredDirectory = new Directory(this.directoryName, this.directoryPath);
        this.files.stream()
                .filter(file -> extensions.stream().anyMatch(ext -> file.getFileName().endsWith(ext)))
                .forEach(file -> {
                    try {
                        filteredDirectory.addFile(file.clone());
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
                });
        this.innerDirectories.forEach(directory -> {
            Directory innerDir = directory.filterExtensions(extensions);
            if (!innerDir.files.isEmpty() || !innerDir.innerDirectories.isEmpty()) {
                filteredDirectory.addDirectory(innerDir);
            }
        });

        return filteredDirectory;
    }
}
