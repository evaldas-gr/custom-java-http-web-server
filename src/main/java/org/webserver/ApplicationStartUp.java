package org.webserver;

import org.webserver.config.AppConfig;
import org.webserver.controllers.IController;
import org.webserver.controllers.media.hls.MediaHlsController;
import org.webserver.handlers.*;
import org.webserver.http.HttpEventDispatcher;
import org.webserver.http.HttpHandlerResolver;
import org.webserver.http.HttpServerThread;
import org.webserver.http.configs.HttpServerConfig;
import org.webserver.http.handlers.HttpHandler;
import org.webserver.media.HlsMediaLoader;
import org.webserver.media.IMediaLoader;
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

public class ApplicationStartUp {

    public static void main(String... args) throws IOException {
        AppConfig.initializeEnv();

        List<IService> services = new ArrayList<>();

        // Services
        MediaCacheService mediaCacheService = new MediaCacheService();
        services.add(mediaCacheService);

        HttpHandlerResolver httpHandlerResolver = new HttpHandlerResolver(new NotFoundHandler());
        httpHandlerResolver.registerHandlers(getHttpHandlers(mediaCacheService));

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

    private static List<HttpHandler> getHttpHandlers(MediaCacheService mediaCacheService) throws IOException {
        List<HttpHandler> httpHandlers = new ArrayList<>();

        // Load resources
        List<Resource> htmlTemplates = ResourceLoader.loadResources("html", "html");
        List<Resource> cssTemplates = ResourceLoader.loadResources("css", "css");
        List<Resource> jsTemplates = ResourceLoader.loadResources("javascript", "js");

        // Http handlers + registers
        httpHandlers.add(new GetRouteDefault("/", ResourceLoader.resolveTemplate("index.html", htmlTemplates)));
        httpHandlers.addAll(cssTemplates.stream().map(handler -> new CssHandler('/' + handler.getFileName(), handler)).toList());
        httpHandlers.addAll(jsTemplates.stream().map(handler -> new JavascriptHandler('/' + handler.getFileName(), handler)).toList());
        for (IMediaLoader mediaLoader : getMediaLoaders())
            httpHandlers.add(new MediaResolverHandler(mediaLoader.getPrefixPath() + "/*/*", mediaLoader.generateMediaKeys(), mediaCacheService));
        for (IController controller : getControllers())
            httpHandlers.addAll(controller.getHandlers());

        return httpHandlers;
    }

    private static List<IMediaLoader> getMediaLoaders() {
        List<IMediaLoader> mediaLoaders = new ArrayList<>();

        HlsMediaLoader hlsMediaLoader = new HlsMediaLoader(AppConfig.getMediaPath());

        mediaLoaders.add(hlsMediaLoader);

        return mediaLoaders;
    }

    private static List<IController> getControllers() throws IOException {
        List<IController> controllers = new ArrayList<>();

        controllers.add(new MediaHlsController(new MediaHlsService(Path.of(AppConfig.getMediaPath()))));

        return controllers;
    }
}
