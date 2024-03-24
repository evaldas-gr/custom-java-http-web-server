package org.webserver.cache;

import org.webserver.media.data.MediaFile;

public interface IInMemoryCache<K extends ICacheKey, T extends MediaFile> {
    public T load(K key);
}
