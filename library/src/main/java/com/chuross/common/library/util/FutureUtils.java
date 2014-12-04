package com.chuross.common.library.util;

import com.google.common.util.concurrent.ListenableFutureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public final class FutureUtils {

    private FutureUtils() {
    }

    public static void executeOrNull(final Executor executor, final Runnable runnable) {
        MethodCallUtils.callQuietly(new Runner() {
            @Override
            public void run() throws Exception {
                executor.execute(runnable);
            }
        });
    }

    public static <V> Future<V> executeOrNull(final Executor executor, final Callable<V> callable) {
        return MethodCallUtils.callOrNull(new Callable<Future<V>>() {
            @Override
            public Future<V> call() throws Exception {
                return execute(executor, callable);
            }
        });
    }

    public static <V> Future<V> execute(final Executor executor, final Callable<V> callable) {
        final FutureTask<V> task = new FutureTask<V>(callable);
        executor.execute(task);
        return task;
    }

    public static <V> Future<V> executeOrNull(final Executor executor, final FutureTask<V> task) {
        return MethodCallUtils.callOrNull(new Callable<Future<V>>() {
            @Override
            public Future<V> call() throws Exception {
                return execute(executor, task);
            }
        });
    }

    public static <V> Future<V> execute(final Executor executor, final FutureTask<V> task) {
        executor.execute(task);
        return task;
    }

    public static <V> ListenableFutureTask<V> executeOrNull(final Executor executor, final ListenableFutureTask<V> task) {
        return MethodCallUtils.callOrNull(new Callable<ListenableFutureTask<V>>() {
            @Override
            public ListenableFutureTask<V> call() throws Exception {
                return execute(executor, task);
            }
        });
    }

    public static <V> ListenableFutureTask<V> execute(final Executor executor, final ListenableFutureTask<V> task) {
        executor.execute(task);
        return task;
    }

    public static <V> V getOrNull(final Future<V> future) {
        return MethodCallUtils.callOrNull(new Callable<V>() {
            @Override
            public V call() throws Exception {
                return future.get();
            }
        });
    }

    public static void cancel(final Future<?> future, final boolean mayInterruptIfRunning) {
        if(future == null) {
            return;
        }
        future.cancel(mayInterruptIfRunning);
    }
}
