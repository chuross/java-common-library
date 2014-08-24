package com.chuross.common.library.api;

import com.chuross.common.library.api.result.Result;
import com.chuross.common.library.http.HttpResponse;
import com.chuross.common.library.util.FutureUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract class AbstractApi<T extends Result<?>> implements Api<T> {

    protected abstract String getUrl();

    protected abstract void setRequestHeaders(List<Header> requestHeaders);

    protected abstract void setParameters(List<NameValuePair> parameters);

    protected abstract Callable<HttpResponse> getHttpRequestCallable(RequestConfig config, int retryCount);

    protected abstract T convert(HttpResponse response) throws Exception;

    protected List<Header> getRequestHeaders() {
        List<Header> requestHeaders = new ArrayList<Header>();
        setRequestHeaders(requestHeaders);
        return requestHeaders;
    }

    protected List<NameValuePair> getParameters() {
        List<NameValuePair> parameters = Lists.newArrayList();
        setParameters(parameters);
        return parameters;
    }

    @Override
    public Future<T> execute(Executor executor, final RequestConfig config, final int retryCount) {
        return FutureUtils.executeOrNull(executor, new Callable<T>() {
            @Override
            public T call() throws Exception {
                return convert(getHttpRequestCallable(config, retryCount).call());
            }
        });
    }

    protected static void addParameterIfNotNull(List<NameValuePair> parameters, String key, Object value) {
        if(StringUtils.isBlank(key) || value == null) {
            return;
        }
        parameters.add(new BasicNameValuePair(key, value.toString()));
    }

}
