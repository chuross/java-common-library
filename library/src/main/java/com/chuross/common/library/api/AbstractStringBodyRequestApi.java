package com.chuross.common.library.api;

import com.chuross.common.library.http.EnclosingRequestParameter;
import com.chuross.common.library.http.HttpResponse;
import com.chuross.common.library.util.FutureUtils;
import com.chuross.common.library.util.HttpClientUtils;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract class AbstractStringBodyRequestApi<T extends Result<?>> extends AbstractApi<T> {

    protected abstract String getBody();

    @Override
    protected void setParameters(List<NameValuePair> parameters) {
    }

    @Override
    public List<NameValuePair> getParameters() {
        return Lists.newArrayList();
    }

    @Override
    protected T executeInExecutor(RequestConfig config, int retryCount) throws Exception {
        switch(getMethod()) {
            case POST:
                return post(config, retryCount);
            case PUT:
                return put(config, retryCount);
            default:
                throw new UnsupportedOperationException("Unsupported method.");
        }
    }

    private T post(RequestConfig config, int retryCount) {
        Executor executor = MoreExecutors.sameThreadExecutor();
        Future<HttpResponse> future = HttpClientUtils.post(executor, getUrl(), new EnclosingRequestParameter(getBody()), getRequestHeaders(), config, retryCount);
        return convert(FutureUtils.getOrNull(future));
    }

    private T put(RequestConfig config, int retryCount) {
        Executor executor = MoreExecutors.sameThreadExecutor();
        Future<HttpResponse> future = HttpClientUtils.put(executor, getUrl(), new EnclosingRequestParameter(getBody()), getRequestHeaders(), config, retryCount);
        return convert(FutureUtils.getOrNull(future));
    }
}
