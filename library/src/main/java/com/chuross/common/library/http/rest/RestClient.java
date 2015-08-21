package com.chuross.common.library.http.rest;

import com.chuross.common.library.http.HttpClient;
import com.chuross.common.library.http.Method;
import com.chuross.common.library.http.Response;
import rx.Observable;
import rx.functions.Func1;

public abstract class RestClient {

    private HttpClient<?> client;

    public RestClient(final HttpClient<?> client) {
        this.client = client;
    }

    protected <RESULT extends Result<?>> Observable<RESULT> execute(final Method method, final RestRequest request, final Func1<Response, RESULT> convertFunc) {
        switch(method) {
            case GET:
                return executeGet(request, convertFunc);
            case POST:
                return executePost(request, convertFunc);
            case PUT:
                return executePut(request, convertFunc);
            case DELETE:
                return executeDelete(request, convertFunc);
            default:
                throw new UnsupportedOperationException("undefined method");
        }
    }

    private <RESULT extends Result<?>> Observable<RESULT> executeGet(final RestRequest request, final Func1<Response, RESULT> convertFunc) {
        return client.get(request.getUrl(), request.getParameters(), request.getRequestHeaders()).map(convertFunc);
    }

    private <RESULT extends Result<?>> Observable<RESULT> executePost(final RestRequest request, final Func1<Response, RESULT> convertFunc) {
        return client.post(request.getUrl(), request.getParameters(), request.getRequestHeaders()).map(convertFunc);
    }

    private <RESULT extends Result<?>> Observable<RESULT> executePut(final RestRequest request, final Func1<Response, RESULT> convertFunc) {
        return client.put(request.getUrl(), request.getParameters(), request.getRequestHeaders()).map(convertFunc);
    }

    private <RESULT extends Result<?>> Observable<RESULT> executeDelete(final RestRequest request, final Func1<Response, RESULT> convertFunc) {
        return client.delete(request.getUrl(), request.getParameters(), request.getRequestHeaders()).map(convertFunc);
    }
}
