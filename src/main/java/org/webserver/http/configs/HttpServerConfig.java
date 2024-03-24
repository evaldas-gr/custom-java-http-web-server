package org.webserver.http.configs;

public class HttpServerConfig {

    public static volatile boolean logsEnabled = false;
    public static int MAX_PATH_LENGTH = 2048;
    public static String HTTP_PROTOCOL_FALLBACK = "HTTP/1.1";
    public static final int HTTP_METHOD_LIMIT = 7;
}
