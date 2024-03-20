package org.webserver.media;

import java.io.IOException;
import java.util.List;

public interface IMediaLoader {
    String getPrefixPath();
    List<MediaKey> generateMediaKeys() throws IOException;
}
