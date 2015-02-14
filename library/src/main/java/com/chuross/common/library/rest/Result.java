package com.chuross.common.library.rest;

import com.chuross.common.library.http.HeaderElement;
import com.google.common.collect.ListMultimap;

public interface Result<T> {

    public int getStatus();

    public boolean isSuccess();

    public ListMultimap<String, HeaderElement> getHeaders();

    public T getContent();
}
