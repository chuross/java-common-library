package com.chuross.common.library.http;

import java.util.concurrent.RunnableFuture;

public interface RequestCommand<R extends Result<?>, C> {

    public RunnableFuture<R> execute(C config);
}
