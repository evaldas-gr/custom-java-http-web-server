package org.webserver.filesystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class DirectoryReader {

    public static List<String> readDirectory(String directoryPath, int maxDepth, List<String> fileExtensions) throws IOException {
        Path basePath = Paths.get(directoryPath);

        return Files.walk(basePath, maxDepth)
                .filter(Files::isRegularFile)
                .filter(path -> fileExtensions.stream().anyMatch(extension -> path.toString().endsWith(extension)))
                .map(path -> basePath.relativize(path).toString())
                .collect(Collectors.toList());
    }

    public static List<String> readDirectoryAbsolutePaths(String directoryPath, int maxDepth, List<String> fileExtensions) throws IOException {
        Path basePath = Paths.get(directoryPath);

        return Files.walk(basePath, maxDepth)
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .filter(string -> fileExtensions.stream().anyMatch(string::endsWith))
                .collect(Collectors.toList());
    }
}
