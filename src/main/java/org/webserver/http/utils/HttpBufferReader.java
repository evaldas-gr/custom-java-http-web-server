package org.webserver.http.utils;

import org.javatuples.Pair;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class HttpBufferReader {

    public static Pair<byte[], Boolean> readUntilAnyOf(ByteBuffer buffer, int limit, byte[] terminateBytes) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean hasExceededLimit = false;

        for (int i = 0; i < buffer.remaining(); i++) {
            byte temp = buffer.get();

            if (containsIn(temp, terminateBytes)) {
                buffer.compact();
                buffer.flip();
                break;
            } else outputStream.write(temp);

            if (i == limit) {
                hasExceededLimit = true;
                break;
            }
        }

        return new Pair<>(outputStream.toByteArray(), hasExceededLimit);
    }

    public static boolean containsIn(byte b, byte[] lookupBytes) {
        for (byte lookupByte : lookupBytes)
            if (b == lookupByte) return true;

        return false;
    }
}
