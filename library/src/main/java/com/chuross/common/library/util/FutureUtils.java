package com.chuross.common.library.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public final class FutureUtils {

    public static <V> Future<V> executeOrNull(Executor executor, Callable<V> callable) {
        try {
            return execute(executor, callable);
        } catch(Exception e) {
            return null;
        }
    }

    public static <V> Future<V> execute(Executor executor, Callable<V> callable) {
        if(executor == null || callable == null) {
            throw new IllegalArgumentException("Null value Executor or Callable");
        }
        FutureTask<V> task = new FutureTask<V>(callable);
        executor.execute(task);
        return task;
    }

    public static <V> Future<V> executeOrNull(Executor executor, FutureTask<V> task) {
        try {
            return execute(executor, task);
        } catch(Exception e) {
            return null;
        }
    }

    public static <V> Future<V> execute(Executor executor, FutureTask<V> task) {
        if(executor == null || task == null) {
            throw new IllegalArgumentException("Null value Executor or FutureTask");
        }
        executor.execute(task);
        return task;
    }

    public static <V> V getOrNull(Future<V> future) {
        try {
            return future.get();
        } catch (Exception e) {
            return null;
        }
    }

}
