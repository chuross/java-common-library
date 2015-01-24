package com.chuross.common.library.util;

import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.concurrent.Callable;

public final class XmlUtils {

    private XmlUtils() {
    }

    public static <T> T read(final Class<T> clazz, final File file) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return new Persister().read(clazz, file);
            }
        });
    }

    public static <T> T read(final Persister persister, final Class<T> clazz, final File file) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return persister.read(clazz, file);
            }
        });
    }

    public static <T> T read(final Class<T> clazz, final File file, final boolean strict) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return new Persister().read(clazz, file, strict);
            }
        });
    }

    public static <T> T read(final Persister persister, final Class<T> clazz, final File file, final boolean strict) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return persister.read(clazz, file, strict);
            }
        });
    }

    public static <T> T read(final Class<T> clazz, final InputStream inputStream) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return new Persister().read(clazz, inputStream);
            }
        });
    }

    public static <T> T read(final Persister persister, final Class<T> clazz, final InputStream inputStream) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return persister.read(clazz, inputStream);
            }
        });
    }

    public static <T> T read(final Class<T> clazz, final InputStream inputStream, final boolean strict) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return new Persister().read(clazz, inputStream, strict);
            }
        });
    }

    public static <T> T read(final Persister persister, final Class<T> clazz, final InputStream inputStream, final boolean strict) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return persister.read(clazz, inputStream, strict);
            }
        });
    }

    public static <T> T read(final Class<T> clazz, final Reader reader) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return new Persister().read(clazz, reader);
            }
        });
    }

    public static <T> T read(final Persister persister, final Class<T> clazz, final Reader reader) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return persister.read(clazz, reader);
            }
        });
    }

    public static <T> T read(final Class<T> clazz, final Reader reader, final boolean strict) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return new Persister().read(clazz, reader, strict);
            }
        });
    }

    public static <T> T read(final Persister persister, final Class<T> clazz, final Reader reader, final boolean strict) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return persister.read(clazz, reader, strict);
            }
        });
    }

    public static <T> T read(final Class<T> clazz, final String source) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return new Persister().read(clazz, source);
            }
        });
    }

    public static <T> T read(final Persister persister, final Class<T> clazz, final String source) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return persister.read(clazz, source);
            }
        });
    }

    public static <T> T read(final Class<T> clazz, final String source, final boolean strict) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return new Persister().read(clazz, source, strict);
            }
        });
    }

    public static <T> T read(final Persister persister, final Class<T> clazz, final String source, final boolean strict) {
        return MethodCallUtils.callOrNull(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return persister.read(clazz, source, strict);
            }
        });
    }
}
