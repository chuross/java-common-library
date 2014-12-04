package com.chuross.common.library.util;

import org.jdeferred.DeferredFutureTask;
import org.jdeferred.Promise;
import org.jdeferred.impl.FutureCallable;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public final class PromiseUtils {

    private PromiseUtils() {
    }

    public static <V, P> Promise<V, Throwable, P> promise(final Executor executor, final Callable<V> callable) {
        final DeferredFutureTask<V, P> task = new DeferredFutureTask<V, P>(callable);
        FutureUtils.executeOrNull(executor, task);
        return task.promise();
    }

    public static <V, P> Promise<V, Throwable, P> promise(final Executor executor, final Future<V> future) {
        final DeferredFutureTask<V, P> task = new DeferredFutureTask<V, P>(new FutureCallable<V>(future));
        FutureUtils.executeOrNull(executor, task);
        return task.promise();
    }

}
