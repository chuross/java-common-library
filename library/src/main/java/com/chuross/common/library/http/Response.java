package com.chuross.common.library.http;

import com.google.common.collect.ListMultimap;

public interface Response {

    int getStatus();

    ListMultimap<String, HeaderElement> getHeaders();

    byte[] getData();
}
