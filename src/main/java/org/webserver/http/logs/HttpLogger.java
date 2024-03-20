package org.webserver.http.logs;

import org.webserver.http.configs.HttpServerConfig;
import org.webserver.http.utils.HttpClockHelper;
import org.javatuples.Triplet;

import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.webserver.http.global.HttpConfig.clock;

public class HttpLogger {

    private static final String serverId = ConsoleColors.ANSI_GREEN + "[HttpServer]" + ConsoleColors.ANSI_RESET;
    private static final String info = ConsoleColors.ANSI_BLUE + "[INFO]" + ConsoleColors.ANSI_RESET;
    private static final String error = ConsoleColors.ANSI_RED + "[ERROR]" + ConsoleColors.ANSI_RESET;

    private static ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    private static final String LOG_FORMAT_1 = "%s" + ConsoleColors.ANSI_YELLOW + "[%s]" + ConsoleColors.ANSI_RESET + "%s %s %s%n";
    public static void log(String message, Class<?> callerObj, Object... params) {
        executorService.execute(() -> {
            if (HttpServerConfig.logsEnabled) {
                String paramsStr = Arrays.stream(params).map(Object::toString).collect(Collectors.joining(","));
                System.out.format(LOG_FORMAT_1, serverId, callerObj.getSimpleName(), info, message, paramsStr);
            }
        });
    }

    public static void log(String message, Object... params) {
        executorService.execute(() -> {
            if (HttpServerConfig.logsEnabled) {
                String paramsStr = Arrays.stream(params).map(Object::toString).collect(Collectors.joining(","));
                System.out.format("%s%s %s %s%n", serverId, info, message, paramsStr);
            }
        });
    }

    public static void error(String message, Object obj, Exception e, Object... params) {
        executorService.execute(() -> {
            if (HttpServerConfig.logsEnabled) {
                String objStr = (obj != null) ? ConsoleColors.ANSI_GREEN + obj.getClass().getSimpleName() + ConsoleColors.ANSI_RESET : "";
                String paramsStr = Arrays.stream(params).map(Object::toString).collect(Collectors.joining(","));
                String exceptionMsg = (e != null) ? e.getMessage() : "";
                System.out.format("%s%s[%s] %s, exception=%s, params=%s%n", serverId, error, objStr, message, exceptionMsg, paramsStr);
            }
        });
    }

    public static void logExecTime(Instant startTime) {
        Instant endTime = clock.instant();
        executorService.execute(() -> {
            if (HttpServerConfig.logsEnabled) {
                Triplet<Long, Integer, Integer> execTime = HttpClockHelper.resolveTime(startTime, endTime);
                System.out.format(String.format("%s%s execution time: %ds, %dms, %dns%n", serverId, info, execTime.getValue0(), execTime.getValue1(), execTime.getValue2()));
            }
        });
    }
}
