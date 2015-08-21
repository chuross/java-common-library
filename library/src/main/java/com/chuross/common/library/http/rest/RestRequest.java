package com.chuross.common.library.http.rest;

import com.chuross.common.library.http.HeaderElement;
import com.google.common.collect.ListMultimap;

public class RestRequest {

    private String url;
    private ListMultimap<String, Object> parameters;
    private ListMultimap<String, HeaderElement> requestHeaders;

    RestRequest(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, HeaderElement> requestHeaders) {
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

    public ListMultimap<String, HeaderElement> getRequestHeaders() {
        return requestHeaders;
    }
}
