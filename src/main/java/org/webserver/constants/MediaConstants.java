package org.webserver.constants;

import java.util.List;

public class MediaConstants {
    public static final String HLS_EXTENSION_M3U8 = ".m3u8";
    public static final String HLS_EXTENSION_M3U = ".m3u";
    public static final String HLS_EXTENSION_TS = ".ts";
    public static final List<String> HLS_EXTENSIONS = List.of(HLS_EXTENSION_M3U8, HLS_EXTENSION_M3U, HLS_EXTENSION_TS);
}
