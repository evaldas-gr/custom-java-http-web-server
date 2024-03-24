package org.webserver.services.media.hls;

import org.webserver.constants.media.MediaHlsConstants;
import org.webserver.filesystem.Directory;
import org.webserver.filesystem.DirectoryParser;
import org.webserver.services.IService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class MediaHlsService implements IService {

    private final Directory hlsDirectory;

    public MediaHlsService(Path directoryPath) throws IOException {
        this.hlsDirectory = DirectoryParser.parseDirectory(directoryPath).filterExtensions(MediaHlsConstants.HLS_EXTENSIONS);
    }

    public Directory getDirectory() {
        return this.hlsDirectory;
    }

    public Optional<Directory> getDirectory(String relativePath) {
        String[] subDirsNames;
        if (relativePath.startsWith("/")) {
            subDirsNames = relativePath.split(Pattern.quote("/"));
        } else {
            subDirsNames = relativePath.substring(1, relativePath.length() - 1).split(Pattern.quote("/"));
        }
        Optional<Directory> currentDir = Optional.of(this.hlsDirectory);
        for (String subDirName : subDirsNames) {
            Optional<Directory> matchingDirectory = currentDir.get().getInnerDirectories().stream()
                    .filter(directory -> directory.getDirectoryName().equals(subDirName))
                    .findFirst();
            if (matchingDirectory.isEmpty()) {
                currentDir = Optional.empty();
                break;
            } else {
                currentDir = matchingDirectory;
            }
        }
        return currentDir;
    }

    public List<Directory> listDirectories() {
        return this.hlsDirectory.getInnerDirectories();
    }

    @Override
    public void startUp() {

    }
}
