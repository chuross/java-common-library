package com.chuross.common.library.util;

import net.arnx.jsonic.JSON;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;

public final class JsonUtils {

    private JsonUtils() {
    }

    public static String encode(final Object source) {
        return ExecuteUtils.callOrNull(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return JSON.encode(source);
            }
        });
    }

    public static String encode(final Object source, final boolean prettyPrint) {
        return ExecuteUtils.callOrNull(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return JSON.encode(source, prettyPrint);
            }
        });
    }

    public static void encode(final Object source, final Appendable appendable) {
        ExecuteUtils.executeQuietly(new Executable() {
            @Override
            public void execute() throws Exception {
                JSON.encode(source, appendable);
            }
        });
    }

    public static void encode(final Object source, final Appendable appendable, final boolean prettyPrint) {
        ExecuteUtils.executeQuietly(new Executable() {
            @Override
            public void execute() throws Exception {
                JSON.encode(source, appendable, prettyPrint);
            }
        });
    }

    public static void encode(final Object source, final OutputStream outputStream) {
        ExecuteUtils.executeQuietly(new Executable() {
            @Override
            public void execute() throws Exception {
                JSON.encode(source, outputStream);
            }
        });
    }

    public static void encode(final Object source, final OutputStream outputStream, final boolean prettyPrint) {
        ExecuteUtils.executeQuietly(new Executable() {
            @Override
            public void execute() throws Exception {
                JSON.encode(source, outputStream, prettyPrint);
            }
        });
    }

    public static <T> T decode(final InputStream inputStream) {
        return ExecuteUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return JSON.decode(inputStream);
            }
        });
    }

    public static <T> T decode(final InputStream inputStream, final Class<T> clazz) {
        return ExecuteUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return JSON.decode(inputStream, clazz);
            }
        });
    }

    public static <T> T decode(final InputStream inputStream, final Type type) {
        return ExecuteUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return JSON.decode(inputStream, type);
            }
        });
    }

    public static <T> T decode(final Reader reader) {
        return ExecuteUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return JSON.decode(reader);
            }
        });
    }

    public static <T> T decode(final Reader reader, final Class<T> clazz) {
        return ExecuteUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return JSON.decode(reader, clazz);
            }
        });
    }

    public static <T> T decode(final Reader reader, final Type type) {
        return ExecuteUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return JSON.decode(reader, type);
            }
        });
    }

    public static <T> T decode(final String source) {
        return ExecuteUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return JSON.decode(source);
            }
        });
    }

    public static <T> T decode(final String source, final Class<T> clazz) {
        return ExecuteUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return JSON.decode(source, clazz);
            }
        });
    }

    public static <T> T decode(final String source, final Type type) {
        return ExecuteUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return JSON.decode(source, type);
            }
        });
    }

    public static String escapeScript(final Object source) {
        return ExecuteUtils.callOrNull(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return JSON.escapeScript(source);
            }
        });
    }

    public static void escapeScript(final Object source, final Appendable appendable) {
        ExecuteUtils.executeQuietly(new Executable() {
            @Override
            public void execute() throws Exception {
                JSON.escapeScript(source, appendable);
            }
        });
    }

    public static void escapeScript(final Object source, final OutputStream outputStream) {
        ExecuteUtils.executeQuietly(new Executable() {
            @Override
            public void execute() throws Exception {
                JSON.escapeScript(source, outputStream);
            }
        });
    }

}
