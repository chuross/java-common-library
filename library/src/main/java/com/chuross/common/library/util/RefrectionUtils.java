package com.chuross.common.library.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class RefrectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefrectionUtils.class);

    private RefrectionUtils() {
    }

    public static <T> T getProperty(final Object target, final String key) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return (T) PropertyUtils.getSimpleProperty(target, key);
            }
        });
    }

    public static void setProperty(final Object target, final String key, final Object value) {
        MethodCallUtils.callQuietly(new Runner() {
            @Override
            public void run() throws Exception {
                PropertyUtils.setProperty(target, key, value);
            }
        });
    }
}
