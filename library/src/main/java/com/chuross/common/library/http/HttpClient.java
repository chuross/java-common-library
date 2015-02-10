package com.chuross.common.library.http;

import com.google.common.collect.ListMultimap;

import java.util.concurrent.RunnableFuture;

public interface HttpClient<R extends Response> {

    public RunnableFuture<R> get(String url, ListMultimap<String, Object> parameters, ListMultimap<String, Object> requestHeaders);

    public RunnableFuture<R> post(String url, ListMultimap<String, Object> parameters, ListMultimap<String, Object> requestHeaders);

    public RunnableFuture<R> post(String url, String requestBody, ListMultimap<String, Object> requestHeaders);

    public RunnableFuture<R> put(String url, ListMultimap<String, Object> parameters, ListMultimap<String, Object> requestHeaders);

    public RunnableFuture<R> put(String url, String requestBody, ListMultimap<String, Object> requestHeaders);

    public RunnableFuture<R> delete(String url, ListMultimap<String, Object> parameters, ListMultimap<String, Object> requestHeaders);
}
