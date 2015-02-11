package com.chuross.common.library.rest;

import com.google.common.collect.ListMultimap;

public class RestRequest {

    private String url;
    private ListMultimap<String, Object> parameters;
    private ListMultimap<String, Object> requestHeaders;

    RestRequest(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, Object> requestHeaders) {
        this.url = url;
        this.parameters = parameters;
        this.requestHeaders = requestHeaders;
    }

    public String getUrl() {
        return url;
    }

    public ListMultimap<String, Object> getParameters() {
        return parameters;
    }

    public ListMultimap<String, Object> getRequestHeaders() {
        return requestHeaders;
    }
}
