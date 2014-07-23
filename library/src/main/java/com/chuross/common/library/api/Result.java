package com.chuross.common.library.api;

public interface Result<T> {

    public boolean isSuccess();

    public int getStatus();

    public T getResult();

}
