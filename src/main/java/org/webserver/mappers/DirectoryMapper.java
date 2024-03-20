package org.webserver.mappers;

import org.webserver.api.media.query.QueryDirectory;
import org.webserver.filesystem.Directory;
import org.webserver.filesystem.File;

import java.util.List;

public class DirectoryMapper {

    public static QueryDirectory toApi(Directory directory) {
        String relativePath = directory.getDirectoryName();
        List<QueryDirectory> dirs = directory.getInnerDirectories().stream().map(DirectoryMapper::toApi).toList();
        List<String> files = directory.getFiles().stream().map(File::getFileName).toList();

        return new QueryDirectory(relativePath, dirs, files);
    }
}
