package com.chuross.common.library.rest;

import com.chuross.common.library.http.HttpClient;
import com.chuross.common.library.http.Response;
import com.google.common.base.Function;
import rx.Observable;
import rx.functions.Func1;

public abstract class RestClient<RESPONSE extends Response> {

    private HttpClient<RESPONSE> client;

    public RestClient(final HttpClient<RESPONSE> client) {
        this.client = client;
    }

    protected <RESULT extends Result<?>> Observable<RESULT> execute(final Method method, final RestRequest request, final Function<RESPONSE, RESULT> convertFunc) {
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

    private <RESULT extends Result<?>> Observable<RESULT> executeGet(final RestRequest request, final Function<RESPONSE, RESULT> convertFunc) {
        return convertObservable(client.get(request.getUrl(), request.getParameters(), request.getRequestHeaders()), convertFunc);
    }

    private <RESULT extends Result<?>> Observable<RESULT> executePost(final RestRequest request, final Function<RESPONSE, RESULT> convertFunc) {
        return convertObservable(client.post(request.getUrl(), request.getParameters(), request.getRequestHeaders()), convertFunc);
    }

    private <RESULT extends Result<?>> Observable<RESULT> executePut(final RestRequest request, final Function<RESPONSE, RESULT> convertFunc) {
        return convertObservable(client.put(request.getUrl(), request.getParameters(), request.getRequestHeaders()), convertFunc);
    }

    private <RESULT extends Result<?>> Observable<RESULT> executeDelete(final RestRequest request, final Function<RESPONSE, RESULT> convertFunc) {
        return convertObservable(client.delete(request.getUrl(), request.getParameters(), request.getRequestHeaders()), convertFunc);
    }

    private <RESULT extends Result<?>> Observable<RESULT> convertObservable(final Observable<RESPONSE> observable, final Function<RESPONSE, RESULT> convertFunc) {
        return observable.map(new Func1<RESPONSE, RESULT>() {
            @Override
            public RESULT call(final RESPONSE response) {
                return convertFunc.apply(response);
            }
        });
    }
}
