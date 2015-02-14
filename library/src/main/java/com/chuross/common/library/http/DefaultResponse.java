package com.chuross.common.library.http;

import com.google.common.collect.ListMultimap;

public class DefaultResponse implements Response {

    private int status;
    private ListMultimap<String, HeaderElement> headers;
    private byte[] data;
    private Exception exception;

    public DefaultResponse(final int status, final ListMultimap<String, HeaderElement> headers, final byte[] data) {
        this.status = status;
        this.headers = headers;
        this.data = data;
    }

    public DefaultResponse(final int status, final ListMultimap<String, HeaderElement> headers, final Exception exception) {
        this.status = status;
        this.headers = headers;
        this.exception = exception;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public ListMultimap<String, HeaderElement> getHeaders() {
        return headers;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    public Exception getException() {
        return exception;
    }
}
