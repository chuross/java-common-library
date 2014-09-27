package com.chuross.common.library.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

public class RefrectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefrectionUtils.class);

    private RefrectionUtils() {
    }

    public static <T> T getProperty(final Object target, final String key) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return (T) PropertyUtils.getProperty(target, key);
            }
        });
    }

    public static <T> T getProperty(final Class<?> clazz, final Object target, final String key, final boolean accessible) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(accessible);
                return (T) field.get(target);
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

    public static void setProperty(final Class<?> clazz, final Object target, final String key, final Object value, final boolean accessible) {
        MethodCallUtils.callQuietly(new Runner() {
            @Override
            public void run() throws Exception {
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(accessible);
                field.set(target, value);
            }
        });
    }
}
