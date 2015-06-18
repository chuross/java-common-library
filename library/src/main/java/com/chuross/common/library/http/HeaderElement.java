package com.chuross.common.library.http;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

public class HeaderElement implements Serializable {

    private String name;
    private String value;

    public HeaderElement(final String value) {
        this(null, value);
    }

    public HeaderElement(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return StringUtils.isBlank(name) ? value : name + "=" + value;
    }

    public static HeaderElement of(final String parameterString) {
        if(StringUtils.isBlank(parameterString)) {
            return null;
        }
        final String[] keyValue = parameterString.split("=");
        return keyValue.length == 1 ? new HeaderElement(normalize(keyValue[0])) : new HeaderElement(keyValue[0].trim(), normalize(keyValue[1]).trim());
    }

    private static String normalize(final String value) {
        return value.endsWith(";") ? value.substring(0, value.length() - 1) : value;
    }

    public static String mergeToString(List<HeaderElement> elements) {
        return Joiner.on(";").join(elements);
    }

    public static String mergeToString(HeaderElement... elements) {
        return Joiner.on(";").join(elements);
    }
}
