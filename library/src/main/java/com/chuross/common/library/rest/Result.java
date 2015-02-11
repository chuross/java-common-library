package com.chuross.common.library.rest;

import com.google.common.collect.ListMultimap;

public interface Result<T> {

    public int getStatus();

    public boolean isSuccess();

    public ListMultimap<String, Object> getHeaders();

    public T getContent();
}
