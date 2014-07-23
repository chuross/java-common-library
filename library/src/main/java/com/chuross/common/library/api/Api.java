package com.chuross.common.library.api;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public interface Api<T> {

    public Future<T> execute(Executor executor);

}
