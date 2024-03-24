package org.webserver.filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class DirectoryParser {
    public static final List<String> systemDirectories = Arrays.asList("META-INF", "target", ".git");

    public static Directory parseDirectory(Path rootDirectory) throws IOException {
        Directory directory = new Directory(rootDirectory.getFileName().toString(), rootDirectory.toString());

        try (Stream<Path> fileStream = Files.walk(rootDirectory, 1)) {
            fileStream
                    .filter(Files::isRegularFile)
                    .map(path -> new File(path.getFileName().toString()))
                    .forEach(directory::addFile);
        }

        try (Stream<Path> directoryStream = Files.walk(rootDirectory, 1)) {
            directoryStream.filter(Files::isDirectory)
                    .filter(path -> !path.equals(rootDirectory) && !isSystemDirectory(path))
                    .map(subDirectoryPath -> {
                        try {
                            return parseDirectory(subDirectoryPath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(directory::addDirectory);
        }

        return directory;
    }

    private static boolean isSystemDirectory(Path directory) {
        return systemDirectories.contains(directory.getFileName().toString());
    }
}
