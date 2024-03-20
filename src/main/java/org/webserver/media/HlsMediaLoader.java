package org.webserver.media;

import org.webserver.config.AppConfig;
import org.webserver.filesystem.DirectoryReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.webserver.constants.MediaConstants.*;
import static org.webserver.http.data.headers.HttpContentTypes.APPLICATION_VND_APPLE_MPEGURL;
import static org.webserver.http.data.headers.HttpHeaders.CONTENT_TYPE;

public class HlsMediaLoader implements IMediaLoader {

    private final String mediaDirectoryPath;

    public HlsMediaLoader(String mediaDirectoryPath) {
        this.mediaDirectoryPath = mediaDirectoryPath;
    }

    @Override
    public String getPrefixPath() {
        return "/hls";
    }

    public List<MediaKey> generateMediaKeys() throws IOException {
        List<String> mediaPaths = DirectoryReader.readDirectory(AppConfig.getMediaPath(), 5, HLS_EXTENSIONS);
        return mediaPaths.stream()
                .filter(mediaPath -> HLS_EXTENSIONS.stream().anyMatch(mediaPath::endsWith))
                .map(mediaRelativePath -> {
                    if (Stream.of(HLS_EXTENSION_M3U8, HLS_EXTENSION_M3U).anyMatch(mediaRelativePath::endsWith)) {
                        return new MediaKey(
                                getPrefixPath() + "/" + mediaRelativePath,
                                mediaDirectoryPath + "/" + mediaRelativePath,
                                new HashMap<>(Map.of(CONTENT_TYPE.getValue(), APPLICATION_VND_APPLE_MPEGURL.getValue()))
                        );
                    } else {
                        return new MediaKey(getPrefixPath() + "/" + mediaRelativePath, mediaDirectoryPath + "/" + mediaRelativePath);
                    }
                }).toList();
    }
}
