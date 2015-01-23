package com.chuross.common.library.http;

import java.util.concurrent.Future;

public interface Invoker<R extends Result<?>> {

    public Future<R> invoke();
}
