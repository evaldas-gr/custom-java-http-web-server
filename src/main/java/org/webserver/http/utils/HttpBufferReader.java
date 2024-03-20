package org.webserver.http.utils;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class HttpBufferReader {

    public static byte[] readUntilAnyOf(ByteBuffer buffer, int limit, byte[] terminateBytes) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        for (int i = 0; i < limit; i++) {
            byte temp = buffer.get();

            if (containsIn(temp, terminateBytes)) {
                buffer.compact();
                buffer.flip();
                break;
            } else outputStream.write(temp);
        }

        return outputStream.toByteArray();
    }

    public static boolean containsIn(byte b, byte[] lookupBytes) {
        for (byte lookupByte : lookupBytes)
            if (b == lookupByte) return true;

        return false;
    }
}
