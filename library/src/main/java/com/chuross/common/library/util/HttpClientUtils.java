package com.chuross.common.library.util;

import com.google.common.collect.ListMultimap;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;

public final class HttpClientUtils {

    private HttpClientUtils() {
    }

    public static String toQueryString(final ListMultimap<String, Object> parameters) {
        if(parameters == null || parameters.isEmpty()) {
            return null;
        }
        final Escaper urlEscape = UrlEscapers.urlPathSegmentEscaper();
        final StringBuilder builder = new StringBuilder();
        CollectionUtils.foreach(parameters, new Procedure<String, Object>() {
            @Override
            public void process(final String key, final Object value) {
                builder.append("&");
                builder.append(key);
                builder.append("=");
                builder.append(urlEscape.escape(value.toString()));
            }
        });
        return builder.toString();
    }
}
