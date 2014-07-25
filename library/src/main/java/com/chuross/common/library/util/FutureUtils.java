package com.chuross.common.library.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public final class FutureUtils {

    private FutureUtils() {
    }

    public static void executeOrNull(final Executor executor, final Runnable runnable) {
        ExecuteUtils.runQuietly(new Runnable() {
            @Override
            public void run() {
                executor.execute(runnable);
            }
        });
    }

    public static <V> Future<V> executeOrNull(final Executor executor, final Callable<V> callable) {
        return ExecuteUtils.callOrNull(new Callable<Future<V>>() {
            @Override
            public Future<V> call() throws Exception {
                return execute(executor, callable);
            }
        });
    }

    public static <V> Future<V> execute(Executor executor, Callable<V> callable) {
        FutureTask<V> task = new FutureTask<V>(callable);
        executor.execute(task);
        return task;
    }

    public static <V> Future<V> executeOrNull(final Executor executor, final FutureTask<V> task) {
        return ExecuteUtils.callOrNull(new Callable<Future<V>>() {
            @Override
            public Future<V> call() throws Exception {
                return execute(executor, task);
            }
        });
    }

    public static <V> Future<V> execute(Executor executor, FutureTask<V> task) {
        executor.execute(task);
        return task;
    }

    public static <V> V getOrNull(final Future<V> future) {
        return ExecuteUtils.callOrNull(new Callable<V>() {
            @Override
            public V call() throws Exception {
                return future.get();
            }
        });
    }

}
