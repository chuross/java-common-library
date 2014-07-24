package com.chuross.common.library.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.Charset;

public final class IOUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    public static String toString(InputStream inputStream) {
        return toString(inputStream, Charset.forName("UTF-8"));
    }

    public static String toString(InputStream inputStream, Charset charset) {
        try {
            return org.apache.commons.io.IOUtils.toString(inputStream, charset);
        } catch(Exception e) {
            LOGGER.error("toString error.", e);
            return null;
        }
    }

}
