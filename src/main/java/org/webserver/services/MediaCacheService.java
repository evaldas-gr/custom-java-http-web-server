package org.webserver.services;

import org.webserver.logs.Logger;
import org.webserver.media.IInMemoryCacheInterface;
import org.webserver.media.MediaFile;
import org.webserver.media.MediaKey;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MediaCacheService implements IInMemoryCacheInterface<MediaKey, MediaFile>, IService {
    private final ConcurrentHashMap<MediaKey, MediaFile> cache = new ConcurrentHashMap<>();
    private ScheduledExecutorService threadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    public MediaFile load(MediaKey key) {
        MediaFile loadedFile = loadFromCache(key);

        if (loadedFile == null) {
            loadedFile = loadFromFileSystem(key);
        }

        if (loadedFile == null) {
            Logger.error("media not found", this, key);
            return null;
        }

        key.refreshTTL();
        cache.put(key, loadedFile);

        return loadedFile;
    }

    private MediaFile loadFromCache(MediaKey key) {
        return cache.get(key);
    }

    public MediaFile loadFromFileSystem(MediaKey key) {
        try {
            Logger.log("cache miss -> reading from file", this, key.getKey());
            String path = key.getMediaResourcePath();
            byte[] content = Files.readAllBytes(Path.of(path));
            Logger.log("media file read success", this, key.getKey());
            return new MediaFile(path, content);
        } catch (Exception e) {
            Logger.error("unexpected load file from system", this, e, key.getKey());
            return null;
        }
    }

    public void startUp() {
        this.threadScheduledExecutor.scheduleAtFixedRate(cacheJob(), 0, 1000, TimeUnit.MILLISECONDS);
    }

    private Runnable cacheJob() {
        return () -> {
            cache.keys().asIterator().forEachRemaining(mediaKey -> {
                if (mediaKey.isExpired()) {
                    cache.remove(mediaKey);
                    Logger.log("removed from cache", this, mediaKey.getKey());
                }
            });
        };
    }
}
