package com.chuross.common.library.api;

import com.chuross.common.library.api.result.Result;
import com.chuross.common.library.http.HttpResponse;
import com.chuross.common.library.util.FutureUtils;
import com.chuross.common.library.util.HttpClientUtils;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.http.client.config.RequestConfig;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public abstract class GetApi<T extends Result<?>> extends AbstractApi<T> {

    @Override
    protected Callable<HttpResponse> getHttpRequestCallable(RequestConfig config, int retryCount) {
        final Future<HttpResponse> future = HttpClientUtils.get(MoreExecutors.sameThreadExecutor(), getUrl(), getParameters(), getRequestHeaders(), config, retryCount);
        return new Callable<HttpResponse>() {
            @Override
            public HttpResponse call() throws Exception {
                return FutureUtils.getOrNull(future);
            }
        };
    }

}