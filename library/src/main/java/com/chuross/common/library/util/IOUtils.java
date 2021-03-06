package com.chuross.common.library.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Callable;

public class IOUtils {

    private IOUtils() {
    }

    public static InputStream toBufferedInputStream(final InputStream input) {
        return MethodCallUtils.callOrNull(new Callable<InputStream>() {
            @Override
            public InputStream call() throws Exception {
                return org.apache.commons.io.IOUtils.toBufferedInputStream(input);
            }
        });
    }

    public static byte[] toByteArray(final InputStream input) {
        return MethodCallUtils.callOrNull(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return org.apache.commons.io.IOUtils.toByteArray(input);
            }
        });
    }

    public static byte[] toByteArray(final InputStream input, final long size) {
        return MethodCallUtils.callOrNull(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return org.apache.commons.io.IOUtils.toByteArray(input, size);
            }
        });
    }

    public static byte[] toByteArray(final InputStream input, final int size) {
        return MethodCallUtils.callOrNull(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return org.apache.commons.io.IOUtils.toByteArray(input, size);
            }
        });
    }

    public static byte[] toByteArray(final Reader input) {
        return MethodCallUtils.callOrNull(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return org.apache.commons.io.IOUtils.toByteArray(input);
            }
        });
    }

    public static byte[] toByteArray(final Reader input, final String encoding) {
        return MethodCallUtils.callOrNull(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return org.apache.commons.io.IOUtils.toByteArray(input, encoding);
            }
        });
    }

    public static byte[] toByteArray(final URI uri) {
        return MethodCallUtils.callOrNull(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return org.apache.commons.io.IOUtils.toByteArray(uri);
            }
        });
    }

    public static byte[] toByteArray(final URL url) {
        return MethodCallUtils.callOrNull(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return org.apache.commons.io.IOUtils.toByteArray(url);
            }
        });
    }

    public static byte[] toByteArray(final URLConnection urlConn) {
        return MethodCallUtils.callOrNull(new Callable<byte[]>() {
            @Override
            public byte[] call() throws Exception {
                return org.apache.commons.io.IOUtils.toByteArray(urlConn);
            }
        });
    }

    public static char[] toCharArray(final InputStream is) {
        return MethodCallUtils.callOrNull(new Callable<char[]>() {
            @Override
            public char[] call() throws Exception {
                return toCharArray(is);
            }
        });
    }

    public static char[] toCharArray(final InputStream is, final Charset encoding) {
        return MethodCallUtils.callOrNull(new Callable<char[]>() {
            @Override
            public char[] call() throws Exception {
                return org.apache.commons.io.IOUtils.toCharArray(is, encoding);
            }
        });
    }

    public static char[] toCharArray(final Reader input) {
        return MethodCallUtils.callOrNull(new Callable<char[]>() {
            @Override
            public char[] call() throws Exception {
                return org.apache.commons.io.IOUtils.toCharArray(input);
            }
        });
    }

    public static String toString(final InputStream input, final Charset encoding) {
        return MethodCallUtils.callOrNull(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return org.apache.commons.io.IOUtils.toString(input, encoding);
            }
        });
    }

    public static String toString(final Reader input) {
        return MethodCallUtils.callOrNull(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return org.apache.commons.io.IOUtils.toString(input);
            }
        });
    }

    public static String toString(final URI uri) {
        return MethodCallUtils.callOrNull(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return org.apache.commons.io.IOUtils.toString(uri);
            }
        });
    }

    public static String toString(final URI uri, final Charset encoding) {
        return MethodCallUtils.callOrNull(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return org.apache.commons.io.IOUtils.toString(uri, encoding);
            }
        });
    }

    public static String toString(final URL url) {
        return MethodCallUtils.callOrNull(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return org.apache.commons.io.IOUtils.toString(url);
            }
        });
    }

    public static String toString(final URL url, final Charset encoding) {
        return MethodCallUtils.callOrNull(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return org.apache.commons.io.IOUtils.toString(url, encoding);
            }
        });
    }

    public static String toString(final byte[] input, final String encoding) {
        return MethodCallUtils.callOrNull(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return org.apache.commons.io.IOUtils.toString(input, encoding);
            }
        });
    }

    public static List<String> readLines(final InputStream input) {
        return MethodCallUtils.callOrNull(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return org.apache.commons.io.IOUtils.readLines(input);
            }
        });
    }

    public static List<String> readLines(final InputStream input, final Charset encoding) {
        return MethodCallUtils.callOrNull(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return org.apache.commons.io.IOUtils.readLines(input, encoding);
            }
        });
    }

    public static List<String> readLines(final Reader input) {
        return MethodCallUtils.callOrNull(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return org.apache.commons.io.IOUtils.readLines(input);
            }
        });
    }

    public static void write(final String data, final OutputStream outputStream) {
        MethodCallUtils.callQuietly(new Runner() {
            @Override
            public void run() throws Exception {
                org.apache.commons.io.IOUtils.write(data, outputStream);
            }
        });
    }
}
