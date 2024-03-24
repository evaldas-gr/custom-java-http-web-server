package org.webserver.media.data;

import org.webserver.cache.ICacheableContent;

public class MediaFile implements ICacheableContent {
    public final String fileName;
    public final byte[] content;

    public MediaFile(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
    }

}
