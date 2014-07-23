package com.chuross.common.library.api;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract class AbstractApi<T> implements Api<T> {

    public abstract String getUrl();

    public abstract Method getMethod();

    public abstract List<Header> getRequestHeader();

    public abstract T convert(HttpResponse response, byte[] data);

    @Override
    public Future<T> execute(final Executor executor) {
        return null;
    }

    public T executeInExecutor() throws Exception {
        switch(getMethod()) {
            case GET:
                return null;
            case POST:
                return null;
            case PUT:
                return null;
            case DELETE:
                return null;
            default:
                throw new IllegalArgumentException();
        }
    }

}
