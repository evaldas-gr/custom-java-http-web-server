package org.webserver.media;

public interface ICacheKey<T> {
    T getKey();
    Integer getTTL();
    boolean isExpired();
}
