package org.webserver.http.executors;

import org.webserver.http.data.HttpResponse;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpOutboundExecutor {
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public static void process(HttpResponse httpResponse, SocketChannel socketChannel) {

    }
}
