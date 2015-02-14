package com.chuross.common.library.http;

import com.google.common.collect.ListMultimap;
import rx.Observable;

public interface HttpClient<R extends Response> {

    public Observable<R> get(String url, ListMultimap<String, Object> parameters, ListMultimap<String, HeaderElement> requestHeaders);

    public Observable<R> post(String url, ListMultimap<String, Object> parameters, ListMultimap<String, HeaderElement> requestHeaders);

    public Observable<R> post(String url, String requestBody, ListMultimap<String, HeaderElement> requestHeaders);

    public Observable<R> put(String url, ListMultimap<String, Object> parameters, ListMultimap<String, HeaderElement> requestHeaders);

    public Observable<R> put(String url, String requestBody, ListMultimap<String, HeaderElement> requestHeaders);

    public Observable<R> delete(String url, ListMultimap<String, Object> parameters, ListMultimap<String, HeaderElement> requestHeaders);
}
