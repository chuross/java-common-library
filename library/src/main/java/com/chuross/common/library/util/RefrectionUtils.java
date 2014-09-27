package com.chuross.common.library.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefrectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefrectionUtils.class);

    private RefrectionUtils() {
    }

    public static <T> T getProperty(Object object, String key) {
        try {
            return (T) PropertyUtils.getSimpleProperty(object, key);
        } catch(Exception e) {
            LOGGER.error("getProperty error", e);
            return null;
        }
    }
}
