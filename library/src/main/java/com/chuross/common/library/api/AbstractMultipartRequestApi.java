package com.chuross.common.library.api;

import com.chuross.common.library.http.EnclosingRequestParameter;
import com.chuross.common.library.http.HttpResponse;
import com.chuross.common.library.util.FutureUtils;
import com.chuross.common.library.util.HttpClientUtils;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.http.client.config.RequestConfig;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public abstract class AbstractMultipartRequestApi<T extends Result<?>> extends AbstractApi<T> {

    protected abstract String getUploadParameterName();

    protected abstract byte[] getUploadData();

    @Override
    protected T executeInExecutor(RequestConfig config, int retryCount) throws Exception {
        switch(getMethod()) {
            case POST:
                return post(config, retryCount);
            default:
                throw new UnsupportedOperationException("Unsupported method.");
        }
    }

    private T post(RequestConfig config, int retryCount) {
        Executor executor = MoreExecutors.sameThreadExecutor();
        Future<HttpResponse> future = HttpClientUtils.post(executor, getUrl(), new EnclosingRequestParameter(getParameters()), getUploadParameterName(), getUploadData(), getRequestHeaders(), config, retryCount);
        return convert(FutureUtils.getOrNull(future));
    }
}
