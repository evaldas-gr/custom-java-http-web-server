package org.webserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Properties;

public class AppConfig {
    private static final Properties properties = new Properties();

    private static int HTTP_PORT = 0;
    private static String HTML_RESOURCE_ABSOLUTE_PATH = null;
    private static boolean LOGGING_STATE = false;
    private static String MEDIA_PATH;

    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static void initializeEnv() {
        try (InputStream input = ClassLoader.getSystemResourceAsStream("config.properties")) {
            ClassLoader classLoader = AppConfig.class.getClassLoader();
            properties.load(input);
            HTTP_PORT = Integer.parseInt(properties.getProperty("http.port"));
            LOGGING_STATE = Boolean.parseBoolean(properties.getProperty("log.enable"));
            MEDIA_PATH = properties.getProperty("media.path");
            HTML_RESOURCE_ABSOLUTE_PATH = Objects.requireNonNull(classLoader.getResource(properties.getProperty("http.resources"))).getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isLoggingEnabled() {
        return LOGGING_STATE;
    }

    public static String getMediaPath() {
        return MEDIA_PATH;
    }

    public static int getHttpPort() {
        return HTTP_PORT;
    }

    public static String getResourceAbsolutePath() {
        return HTML_RESOURCE_ABSOLUTE_PATH;
    }

    public static String getLocalHostAddressIpv4() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
}
