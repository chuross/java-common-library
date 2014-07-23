package com.chuross.common.library.api;

import org.apache.http.client.config.RequestConfig;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public interface Api<T extends Result<?>> {

    public Future<T> execute(Executor executor, RequestConfig config, int retryCount);

}
