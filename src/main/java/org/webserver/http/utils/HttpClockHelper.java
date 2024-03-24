package org.webserver.http.utils;

import org.javatuples.Triplet;

import java.time.Instant;

public class HttpClockHelper {
    public static Triplet<Long, Integer, Integer> resolveTime(Instant startTime, Instant endTime) {
        int nanos = (endTime.getNano() - startTime.getNano()) / 1000;
        int millis = nanos / 1000;
        return new Triplet<>(endTime.getEpochSecond() - startTime.getEpochSecond(), millis, nanos % 1000);
    }
}
