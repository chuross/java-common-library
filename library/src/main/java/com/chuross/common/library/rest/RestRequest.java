package com.chuross.common.library.rest;

import com.google.common.collect.ListMultimap;

public class RestRequest {

    private String path;
    private ListMultimap<String, Object> parameters;
    private ListMultimap<String, Object> requestHeaders;

    RestRequest(final String path, final ListMultimap<String, Object> parameters, final ListMultimap<String, Object> requestHeaders) {
        this.path = path;
        this.parameters = parameters;
        this.requestHeaders = requestHeaders;
    }

    public String getPath() {
        return path;
    }

    public ListMultimap<String, Object> getParameters() {
        return parameters;
    }

    public ListMultimap<String, Object> getRequestHeaders() {
        return requestHeaders;
    }
}
