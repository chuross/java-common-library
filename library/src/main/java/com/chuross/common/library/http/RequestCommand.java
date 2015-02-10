package com.chuross.common.library.http;

import java.util.concurrent.RunnableFuture;

public interface RequestCommand<R extends Result<?>> {

    public RunnableFuture<R> execute();
}
