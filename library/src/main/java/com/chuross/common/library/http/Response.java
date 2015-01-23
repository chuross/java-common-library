package com.chuross.common.library.http;

import com.google.common.collect.ListMultimap;

import java.io.InputStream;

public interface Response {

    public int getStatus();

    public ListMultimap<String, Object> getHeaders();

    public InputStream getInputStream();
}
