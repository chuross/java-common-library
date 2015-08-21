package com.chuross.common.library.http.rest;

import com.chuross.common.library.http.HeaderElement;
import com.google.common.collect.ListMultimap;

public interface Result<T> {

    int getStatus();

    boolean isSuccess();

    ListMultimap<String, HeaderElement> getHeaders();

    T getContent();
}
