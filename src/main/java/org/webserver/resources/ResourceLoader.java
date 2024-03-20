package org.webserver.resources;

import org.webserver.config.AppConfig;
import org.webserver.filesystem.DirectoryReader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ResourceLoader {

    public static List<Resource> loadResources(String relativePath, String... extensions) throws IOException {
        String resourceAbsolutePath = AppConfig.getResourceAbsolutePath();
        if (relativePath != null && !relativePath.trim().isBlank()) {
            resourceAbsolutePath += File.separator + relativePath;
        }

        Stream<Path> resourceAbsolutePaths = DirectoryReader.readDirectoryAbsolutePaths(resourceAbsolutePath, 5, List.of(extensions)).stream().map(Path::of);
        List<File> files = resourceAbsolutePaths
                .map(Path::toAbsolutePath)
                .map(absolutePath -> new File(absolutePath.toString()))
                .toList();

        List<Resource> resources = new ArrayList<>();

        for (File file : files) {
            FileChannel channel = new FileInputStream(file.getAbsolutePath()).getChannel();
            long fileSize = channel.size();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            while (buffer.hasRemaining()) {
                byte[] contentChunk = new byte[buffer.remaining()];
                buffer.get(contentChunk);
                stream.write(contentChunk);
            }
            resources.add(new Resource(file.getName(), stream.toByteArray()));
        }

        return resources;
    }

    public static Resource resolveTemplate(String fileName, List<Resource> templates) {
        return templates.stream().filter(template -> Objects.equals(template.getFileName(), fileName)).findAny().orElseThrow();
    }
}
