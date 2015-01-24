package com.chuross.common.library.http;

import com.google.common.collect.ListMultimap;

public interface Response {

    public int getStatus();

    public ListMultimap<String, Object> getHeaders();

    public byte[] getData();
}
