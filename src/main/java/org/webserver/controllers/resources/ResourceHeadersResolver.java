package org.webserver.controllers.resources;

import java.util.Map;

import static org.webserver.http.data.types.HttpContentType.*;
import static org.webserver.http.data.types.HttpHeader.CONTENT_TYPE;

public class ResourceHeadersResolver {

    public static Map<String, String> resolveHeaders(String fileName) {
        if (fileName.endsWith(".html")) {
            return Map.of(CONTENT_TYPE.getValue(), APPLICATION_TEXT_HTML.getValue());
        } else if (fileName.endsWith(".css")) {
            return Map.of(CONTENT_TYPE.getValue(), APPLICATION_TEXT_CSS.getValue());
        } else if (fileName.endsWith(".js")) {
            return Map.of(CONTENT_TYPE.getValue(), APPLICATION_TEXT_JAVASCRIPT.getValue());
        } else {
            return Map.of();
        }
    }
}
