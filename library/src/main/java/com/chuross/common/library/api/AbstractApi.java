package com.chuross.common.library.api;

import com.chuross.common.library.http.HttpResponse;
import com.chuross.common.library.util.FutureUtils;
import com.chuross.common.library.util.HttpClientUtils;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract class AbstractApi<T extends Result<?>> implements Api<T> {

    public abstract String getUrl();

    public abstract Method getMethod();

    protected abstract void setParameters(List<NameValuePair> parameters);

    protected abstract void setRequestHeaders(List<Header> requestHeaders);

    protected abstract T convert(HttpResponse response);

    public List<NameValuePair> getParameters() {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        setParameters(parameters);
        return parameters;
    }

    public List<Header> getRequestHeaders() {
        List<Header> requestHeaders = new ArrayList<Header>();
        setRequestHeaders(requestHeaders);
        return requestHeaders;
    }

    @Override
    public Future<T> execute(Executor executor, final RequestConfig config, final int retryCount) {
        return FutureUtils.executeOrNull(executor, new Callable<T>() {
            @Override
            public T call() throws Exception {
                return executeInExecutor(config, retryCount);
            }
        });
    }

    protected T executeInExecutor(RequestConfig config, int retryCount) throws Exception {
        switch(getMethod()) {
            case GET:
                return get(config, retryCount);
            case POST:
                return post(config, retryCount);
            case PUT:
                return put(config, retryCount);
            case DELETE:
                return delete(config, retryCount);
            default:
                throw new IllegalArgumentException();
        }
    }

    private T get(RequestConfig config, int retryCount) {
        Executor executor = MoreExecutors.sameThreadExecutor();
        Future<HttpResponse> future = HttpClientUtils.get(executor, getUrl(), getParameters(), getRequestHeaders(), config, retryCount);
        return convert(FutureUtils.getOrNull(future));
    }

    private T post(RequestConfig config, int retryCount) {
        Executor executor = MoreExecutors.sameThreadExecutor();
        Future<HttpResponse> future = HttpClientUtils.post(executor, getUrl(), getParameters(), getRequestHeaders(), config, retryCount);
        return convert(FutureUtils.getOrNull(future));
    }

    private T put(RequestConfig config, int retryCount) {
        Executor executor = MoreExecutors.sameThreadExecutor();
        Future<HttpResponse> future = HttpClientUtils.put(executor, getUrl(), getParameters(), getRequestHeaders(), config, retryCount);
        return convert(FutureUtils.getOrNull(future));
    }

    private T delete(RequestConfig config, int retryCount) {
        Executor executor = MoreExecutors.sameThreadExecutor();
        Future<HttpResponse> future = HttpClientUtils.delete(executor, getUrl(), getParameters(), getRequestHeaders(), config, retryCount);
        return convert(FutureUtils.getOrNull(future));
    }

}
