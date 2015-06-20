package com.chuross.common.library.rest;

import com.chuross.common.library.http.HeaderElement;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.net.HttpHeaders;

public class RestRequestBuilder {

    private String url;
    private ListMultimap<String, Object> parameters = LinkedListMultimap.create();
    private ListMultimap<String, HeaderElement> requestHeaders = LinkedListMultimap.create();

    public RestRequestBuilder(final String url) {
        this.url = url;
    }

    public RestRequestBuilder setUserAgent(final String value) {
        if(requestHeaders.containsKey(HttpHeaders.USER_AGENT)) {
            requestHeaders.removeAll(HttpHeaders.USER_AGENT);
        }
        requestHeaders.put(HttpHeaders.USER_AGENT, HeaderElement.of(value));
        return this;
    }

    public RestRequestBuilder addCookie(final Object value) {
        return addCookie(HeaderElement.of(value.toString()));
    }

    public RestRequestBuilder addCookie(final HeaderElement element) {
        requestHeaders.put(HttpHeaders.COOKIE, element);
        return this;
    }

    public RestRequestBuilder addParameter(final String key, final Object value) {
        parameters.put(key, value);
        return this;
    }

    public RestRequestBuilder addParameterIfNotNull(final String key, final Object value) {
        if(value != null) {
            addParameter(key, value);
        }
        return this;
    }

    public RestRequestBuilder addRequestHeader(final String key, final HeaderElement element) {
        requestHeaders.put(key, element);
        return this;
    }

    public RestRequestBuilder addRequestHeader(final String key, final Object value) {
        requestHeaders.put(key, HeaderElement.of(value.toString()));
        return this;
    }

    public RestRequestBuilder addRequestHeaderIfNotNull(final String key, final Object value) {
        if(value != null) {
            addRequestHeader(key, value);
        }
        return this;
    }

    public RestRequest build() {
        return new RestRequest(url, parameters, requestHeaders);
    }
}
