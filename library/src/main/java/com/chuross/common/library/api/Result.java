package com.chuross.common.library.api;

import org.apache.http.Header;

import java.util.List;

public interface Result<T> {

    public boolean isSuccess();

    public int getStatus();

    public List<Header> getHeaders();

    public T getResult();

}
