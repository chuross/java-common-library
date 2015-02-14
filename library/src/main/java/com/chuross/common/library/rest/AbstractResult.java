package com.chuross.common.library.rest;

import com.chuross.common.library.http.HeaderElement;
import com.google.common.collect.ListMultimap;

import java.net.HttpURLConnection;

public abstract class AbstractResult<T> implements Result<T> {

    private int status;
    private ListMultimap<String, HeaderElement> responseHeaders;
    private T content;

    public AbstractResult(final int status, final ListMultimap<String, HeaderElement> responseHeaders, final T content) {
        this.status = status;
        this.responseHeaders = responseHeaders;
        this.content = content;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public boolean isSuccess() {
        return status == HttpURLConnection.HTTP_OK;
    }

    @Override
    public ListMultimap<String, HeaderElement> getHeaders() {
        return responseHeaders;
    }

    @Override
    public T getContent() {
        return content;
    }
}
