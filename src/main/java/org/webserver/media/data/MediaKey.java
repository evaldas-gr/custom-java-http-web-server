package org.webserver.media.data;

import org.webserver.cache.ICacheKey;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.webserver.config.AppConfig.MEDIA_KEY_TTL;

public class MediaKey implements ICacheKey<String> {
    private final String key;
    private final String mediaResourcePath;

    public String getMediaRelativePath() {
        return mediaRelativePath;
    }

    private final String mediaRelativePath;
    private final Integer ttl; // in ms
    private final Map<String, String> explicitHeaders;
    private Instant latestActionTime = Instant.now();

    @Override
    public String toString() {
        return "MediaKey{" +
                "key='" + key + '\'' +
                ", mediaResourcePath='" + mediaResourcePath + '\'' +
                ", ttl=" + ttl +
                ", explicitHeaders=" + explicitHeaders +
                ", latestActionTime=" + latestActionTime +
                '}';
    }

    public MediaKey(String key, String mediaResourcePath) {
        this.key = key;
        this.mediaResourcePath = mediaResourcePath;
        this.mediaRelativePath = key;
        this.ttl = MEDIA_KEY_TTL;
        this.explicitHeaders = new HashMap<>();
    }

    public MediaKey(String key, String mediaResourcePath, Map<String, String> explicitHeaders) {
        this.key = key;
        this.mediaResourcePath = mediaResourcePath;
        this.mediaRelativePath = key;
        this.ttl = MEDIA_KEY_TTL;
        this.explicitHeaders = explicitHeaders;
    }

    public synchronized String getKey() {
        latestActionTime = Instant.now();
        return this.key;
    }

    public String getMediaResourcePath() {
        return mediaResourcePath;
    }


    @Override
    public Integer getTTL() {
        return this.ttl;
    }

    public Map<String, String> getHeaders() {
        return explicitHeaders;
    }

    public boolean isExpired() {
        synchronized (this) {
            return latestActionTime.plusMillis(ttl).isBefore(Instant.now());
        }
    }

    public synchronized void refreshTTL() {
        latestActionTime = Instant.now();
    }
}
