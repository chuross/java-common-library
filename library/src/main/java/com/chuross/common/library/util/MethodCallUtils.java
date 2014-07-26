package com.chuross.common.library.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public final class MethodCallUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodCallUtils.class);

    private MethodCallUtils() {
    }

    public static void callQuietly(Executable executable) {
        try {
            executable.execute();
        } catch(Throwable tr) {
            LOGGER.error("callQuietly error.", tr);
        }
    }

    public static <T> T callOrNull(Callable<T> callable) {
        try {
            return callable.call();
        } catch(Throwable tr) {
            LOGGER.error("call error.", tr);
            return null;
        }
    }

    public static <T> T callOrSubstitute(Callable<T> callable, Callable<T> failCallable) {
        try {
            return callable.call();
        } catch(Throwable tr) {
            LOGGER.error("call error.", tr);
            return callOrNull(failCallable);
        }
    }

}
