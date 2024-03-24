package org.webserver;

import org.webserver.config.AppConfig;
import org.webserver.controllers.IController;
import org.webserver.controllers.media.hls.MediaHlsController;
import org.webserver.controllers.resources.ResourceController;
import org.webserver.handlers.NotFoundHandler;
import org.webserver.http.HttpEventDispatcher;
import org.webserver.http.HttpHandlerResolver;
import org.webserver.http.HttpServerThread;
import org.webserver.http.configs.HttpServerConfig;
import org.webserver.http.handlers.HttpHandler;
import org.webserver.media.HlsMediaLoader;
import org.webserver.resources.Resource;
import org.webserver.resources.ResourceLoader;
import org.webserver.services.IService;
import org.webserver.services.MediaCacheService;
import org.webserver.services.media.hls.MediaHlsService;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ApplicationStartUp {

    public static void main(String... args) throws IOException {
        AppConfig.initializeEnv();

        List<IService> services = new ArrayList<>();

        // Services
        MediaCacheService mediaCacheService = new MediaCacheService();
        MediaHlsService mediaHlsService = new MediaHlsService(Path.of(AppConfig.getMediaPath()));
        services.add(mediaCacheService);
        services.add(mediaHlsService);

        HttpHandlerResolver httpHandlerResolver = new HttpHandlerResolver(new NotFoundHandler());
        httpHandlerResolver.registerHandlers(loadHttpHandlers(mediaCacheService, mediaHlsService).toList());

        // Custom Http Server
        HttpServerConfig.logsEnabled = AppConfig.isLoggingEnabled();
        HttpEventDispatcher eventDispatcher = new HttpEventDispatcher(httpHandlerResolver);
        HttpServerThread httpServerThread = new HttpServerThread(
                AppConfig.getLocalHostAddressIpv4(),
                AppConfig.getHttpPort(),
                StandardProtocolFamily.INET,
                eventDispatcher);

        // Startup
        services.forEach(IService::startUp);
        httpServerThread.start();
    }

    private static Stream<HttpHandler> loadHttpHandlers(MediaCacheService mediaCacheService, MediaHlsService mediaHlsService) throws IOException {
        return loadControllers(mediaCacheService, mediaHlsService)
                .map(IController::getHandlers)
                .reduce(Stream::concat)
                .orElseGet(Stream::empty);
    }

    private static Stream<IController> loadControllers(
            MediaCacheService mediaCacheService,
            MediaHlsService mediaHlsService
    ) throws IOException {
        return Stream.of(
                new ResourceController(loadResources()),
                new MediaHlsController(
                        mediaHlsService,
                        mediaCacheService,
                        new HlsMediaLoader(AppConfig.getMediaPath())
                )
        );
    }

    public static List<Resource> loadResources() throws IOException {
        List<Resource> resources = new ArrayList<>();

        List<Resource> htmlResources = ResourceLoader.loadResources("html", "/", "html").stream().peek(resource -> {
            if (resource.getFileName().equals("index.html")) {
                resource.setDefaultPath(true);
            }
        }).toList();
        List<Resource> cssResources = ResourceLoader.loadResources("css", "/css", "css");
        List<Resource> jsResources = ResourceLoader.loadResources("javascript", "/javascript", "js");

        resources.addAll(htmlResources);
        resources.addAll(cssResources);
        resources.addAll(jsResources);

        return resources;
    }
}
