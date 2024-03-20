package org.webserver.logs;

import org.webserver.config.AppConfig;
import org.webserver.http.logs.ConsoleColors;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Logger {
    private static final String serverId = ConsoleColors.ANSI_GREEN + "[APP]" + ConsoleColors.ANSI_RESET;
    private static final String info = ConsoleColors.ANSI_BLUE + "[INFO]" + ConsoleColors.ANSI_RESET;
    private static final String error = ConsoleColors.ANSI_RED + "[ERROR]" + ConsoleColors.ANSI_RESET;

    private static ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    private static final String LOG_FORMAT_1 = "%s" + ConsoleColors.ANSI_YELLOW + "[%s]" + ConsoleColors.ANSI_RESET + "%s %s %s%n";

    public static void log(String message, Object callerObj, Object... params) {
        executorService.execute(() -> {
            if (AppConfig.isLoggingEnabled()) {
                String paramsStr = Arrays.stream(params).map(Object::toString).collect(Collectors.joining(","));
                System.out.format(LOG_FORMAT_1, serverId, callerObj.getClass().getSimpleName(), info, message, paramsStr);
            }
        });
    }

    public static void log(String message) {
        executorService.execute(() -> {
            if (AppConfig.isLoggingEnabled()) {
                System.out.format("%s %s%n", serverId, message);
            }
        });
    }

    public static void error(String message) {
        executorService.execute(() -> {
            if (AppConfig.isLoggingEnabled()) {
                System.out.format("%s%s %s%n", serverId, error, message);
            }
        });
    }

    public static void error(String message, Object callerObj, Object... params) {
        executorService.execute(() -> {
            if (AppConfig.isLoggingEnabled()) {
                String objStr = (callerObj != null) ? ConsoleColors.ANSI_GREEN + callerObj.getClass().getSimpleName() + ConsoleColors.ANSI_RESET : "";
                String paramsStr = Arrays.stream(params).map(Object::toString).collect(Collectors.joining(","));
                System.out.format("%s%s[%s] %s, params=%s%n", serverId, error, objStr, message, paramsStr);
            }
        });
    }

    public static void error(String message, Object callerObj, Exception e, Object... params) {
        executorService.execute(() -> {
            if (AppConfig.isLoggingEnabled()) {
                String objStr = (callerObj != null) ? ConsoleColors.ANSI_GREEN + callerObj.getClass().getSimpleName() + ConsoleColors.ANSI_RESET : "";
                String paramsStr = Arrays.stream(params).map(Object::toString).collect(Collectors.joining(","));
                String exceptionMsg = (e != null) ? e.getMessage() : "";
                System.out.format("%s%s[%s] %s, exception=%s, params=%s%n", serverId, error, objStr, message, exceptionMsg, paramsStr);
            }
        });
    }
}
