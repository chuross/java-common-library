package com.chuross.common.library.api;

import org.apache.http.Header;

import java.net.HttpURLConnection;
import java.util.List;

public abstract class AbstractResult<T> implements Result<T> {

    private int status;
    private List<Header> headers;
    private T result;

    public AbstractResult(int status, List<Header> headers, T result) {
        this.status = status;
        this.headers = headers;
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public T getResult() {
        return result;
    }

    public boolean isBadRequest() {
        return status == HttpURLConnection.HTTP_BAD_REQUEST;
    }

    public boolean isUnauthorized() {
        return status == HttpURLConnection.HTTP_UNAUTHORIZED;
    }

    public boolean isForbidden() {
        return status == HttpURLConnection.HTTP_FORBIDDEN;
    }

    public boolean isNotFound() {
        return status == HttpURLConnection.HTTP_NOT_FOUND;
    }

    public boolean isInternalError() {
        return status == HttpURLConnection.HTTP_INTERNAL_ERROR;
    }

    public boolean isUnavailable() {
        return status == HttpURLConnection.HTTP_UNAVAILABLE;
    }

}
