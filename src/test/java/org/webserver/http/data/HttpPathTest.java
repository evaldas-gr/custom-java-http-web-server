package org.webserver.http.data;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpPathTest {

    @ParameterizedTest
    @MethodSource
    void checkHttpPathMatchMethodSource(boolean matchValue, String basePath, String clientPath) {
        HttpPath serverHttp = new HttpPath(basePath);
        HttpPath clientHttp = new HttpPath(clientPath);
        assertEquals(matchValue, serverHttp.isMatch(clientHttp));
    }

    static Stream<Arguments> checkHttpPathMatchMethodSource() {
        return Stream.of(
                Arguments.of(false, "/123", "/12345"),
                Arguments.of(false, "/*/qwdas", "/"),
                Arguments.of(false, "/123", "/"),
                Arguments.of(false, "/123", "/124"),
                Arguments.of(false, "/*", "/"),
                Arguments.of(false, "/*", "/a/b"),

                Arguments.of(true, "*", "/"),
                Arguments.of(true, "/123", "/123"),
                Arguments.of(true, "/*/123as", "/bb/123as"),
                Arguments.of(true, "/*", "/123asd"),
                Arguments.of(true, "/*", "/1"),
                Arguments.of(true, "/*", "/123asd/"),
                Arguments.of(true, "/v3/*", "/v3/test.h3u8"),
                Arguments.of(true, "/hls/*/*", "/hls/v3/test.m3u8")
        );
    }
}
