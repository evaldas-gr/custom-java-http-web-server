package org.webserver.http.executors;

import org.webserver.http.HttpHandlerResolver;
import org.webserver.http.data.HttpMetadata;
import org.webserver.http.data.HttpRequest;
import org.webserver.http.data.HttpResponse;
import org.webserver.http.exceptions.HttpException;
import org.webserver.http.exceptions.utils.HttpExceptionsHandler;
import org.webserver.http.handlers.HttpHandler;
import org.webserver.http.logs.HttpLogger;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static org.webserver.http.global.HttpConfig.clock;

public class HttpInboundExecutor {

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    private final HttpHandlerResolver httpHandlerResolver;

    public HttpInboundExecutor(HttpHandlerResolver httpHandlerResolver) {
        this.httpHandlerResolver = httpHandlerResolver;
    }

    public void process(SocketChannel clientChannel, byte[] socketData, Consumer<HttpResponse> submitResponse) {
        executorService.execute(() -> {
            Instant startTime = clock.instant();
            HttpResponse response;
            HttpMetadata metadata = null;
            HttpHandler handler = null;
            try {
                HttpRequest httpRequest = HttpRequest.parseRequest(socketData);
                HttpLogger.log("request from: ", clientChannel.getRemoteAddress());
                HttpLogger.log("request data: ", httpRequest);
                metadata = httpRequest.getMetadata();
                handler = this.httpHandlerResolver.getHandler(httpRequest);

                response = handler.handle(httpRequest);
            } catch (HttpException httpException) {
                response = HttpExceptionsHandler.resolveResponse(httpException, metadata);
            } catch (ClosedChannelException e) {
                HttpLogger.error("channel closed while handling", this, e);
                return;
            } catch (IOException e) {
                HttpLogger.error("failed to resolve socketChannel remote address", this, e);
                return;
            }

            HttpLogger.log("resolved handler: " + handler + '\'');
            HttpLogger.log("server response: " + response);
            HttpLogger.logExecTime(startTime);

            submitResponse.accept(response);
        });
    }
}
