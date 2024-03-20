package org.webserver.media;

public interface IInMemoryCacheInterface<K extends ICacheKey, T extends MediaFile> {
    public T load(K key);
}
