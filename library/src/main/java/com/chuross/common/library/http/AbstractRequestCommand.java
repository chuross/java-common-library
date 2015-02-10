package com.chuross.common.library.http;

import com.chuross.common.library.util.FutureUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public abstract class AbstractRequestCommand<R extends Result<?>, C> implements RequestCommand<R> {

    private HttpClient<Response> client;

    public AbstractRequestCommand(final HttpClient client) {
        this.client = client;
    }

    protected abstract Method getMethod();

    protected abstract String getUrl();

    protected abstract ListMultimap<String, Object> setParameters(ListMultimap<String, Object> parameters);

    protected abstract ListMultimap<String, Object> setRequestHeaders(ListMultimap<String, Object> requestHeaders);

    protected abstract R convert(Response response);

    protected String getBody() {
        return null;
    }

    protected ListMultimap<String, Object> getParameters() {
        final ListMultimap<String, Object> parameters = ArrayListMultimap.create();
        setParameters(parameters);
        return parameters;
    }

    protected ListMultimap<String, Object> getRequestHeaders() {
        final ListMultimap<String, Object> requestHeaders = ArrayListMultimap.create();
        setRequestHeaders(requestHeaders);
        return requestHeaders;
    }

    @Override
    public RunnableFuture<R> execute() {
        final Future<Response> clientFuture = executeClient();
        return new FutureTask<R>(new Callable<R>() {
            @Override
            public R call() throws Exception {
                return convert(FutureUtils.getOrNull(clientFuture));
            }
        }) {
            @Override
            public boolean cancel(final boolean mayInterruptIfRunning) {
                FutureUtils.cancel(clientFuture, mayInterruptIfRunning);
                return super.cancel(mayInterruptIfRunning);
            }
        };
    }

    private Future<Response> executeClient() {
        switch(getMethod()) {
            case GET:
                return executeGet();
            case POST:
                return !StringUtils.isBlank(getBody()) ? executePostAsBody() : executePost();
            case PUT:
                return !StringUtils.isBlank(getBody()) ? executePutAsBody() : executePut();
            case DELETE:
                return executeDelete();
            default:
                throw new IllegalArgumentException("invalid method");
        }
    }

    private Future<Response> executeGet() {
        return FutureUtils.execute(MoreExecutors.sameThreadExecutor(), client.get(getUrl(), getParameters(), getRequestHeaders()));
    }

    private Future<Response> executePost() {
        return FutureUtils.execute(MoreExecutors.sameThreadExecutor(), client.post(getUrl(), getParameters(), getRequestHeaders()));
    }

    private Future<Response> executePostAsBody() {
        return FutureUtils.execute(MoreExecutors.sameThreadExecutor(), client.post(getUrl(), getBody(), getRequestHeaders()));
    }

    private Future<Response> executePut() {
        return FutureUtils.execute(MoreExecutors.sameThreadExecutor(), client.put(getUrl(), getParameters(), getRequestHeaders()));
    }

    private Future<Response> executePutAsBody() {
        return FutureUtils.execute(MoreExecutors.sameThreadExecutor(), client.put(getUrl(), getBody(), getRequestHeaders()));
    }

    private Future<Response> executeDelete() {
        return FutureUtils.execute(MoreExecutors.sameThreadExecutor(), client.delete(getUrl(), getParameters(), getRequestHeaders()));
    }
}
