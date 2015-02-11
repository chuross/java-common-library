package com.chuross.common.library.rest;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.net.HttpHeaders;

public class RestRequestBuilder {

    private String url;
    private ListMultimap<String, Object> parameters = ArrayListMultimap.create();
    private ListMultimap<String, Object> requestHeaders = ArrayListMultimap.create();

    public RestRequestBuilder(final String url) {
        this.url = url;
    }

    public RestRequestBuilder setUserAgent(final String value) {
        if(requestHeaders.containsKey(HttpHeaders.USER_AGENT)) {
            requestHeaders.removeAll(HttpHeaders.USER_AGENT);
        }
        requestHeaders.put(HttpHeaders.USER_AGENT, value);
        return this;
    }

    public RestRequestBuilder addParameter(final String key, final Object value) {
        parameters.put(key, value);
        return this;
    }

    public RestRequestBuilder addRequestHeader(final String key, final Object value) {
        requestHeaders.put(key, value);
        return this;
    }

    public RestRequest build() {
        return new RestRequest(url, parameters, requestHeaders);
    }
}
