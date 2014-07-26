package com.chuross.common.library.api;

public abstract class AbstractResult<T> implements Result<T> {

    private int status;

    private T result;

    public AbstractResult(int status, T result) {
        this.status = status;
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public T getResult() {
        return result;
    }

}
