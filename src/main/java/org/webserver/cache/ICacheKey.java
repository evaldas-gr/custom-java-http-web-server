package org.webserver.cache;

public interface ICacheKey<T> {
    T getKey();
    Integer getTTL();
    boolean isExpired();
}
