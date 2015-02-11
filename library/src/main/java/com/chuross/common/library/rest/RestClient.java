package com.chuross.common.library.rest;

import com.chuross.common.library.http.HttpClient;
import com.chuross.common.library.http.Response;
import com.google.common.base.Function;
import rx.Observable;
import rx.functions.Func1;

import java.util.Locale;

public abstract class RestClient<RESPONSE extends Response> {

    private HttpClient<RESPONSE> client;

    public RestClient(final HttpClient<RESPONSE> client) {
        this.client = client;
    }

    protected abstract RestContext getContext();

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
        return convertObservable(client.get(getUrl(request.getPath()), request.getParameters(), request.getRequestHeaders()), convertFunc);
    }

    private <RESULT extends Result<?>> Observable<RESULT> executePost(final RestRequest request, final Function<RESPONSE, RESULT> convertFunc) {
        return convertObservable(client.post(getUrl(request.getPath()), request.getParameters(), request.getRequestHeaders()), convertFunc);
    }

    private <RESULT extends Result<?>> Observable<RESULT> executePut(final RestRequest request, final Function<RESPONSE, RESULT> convertFunc) {
        return convertObservable(client.put(getUrl(request.getPath()), request.getParameters(), request.getRequestHeaders()), convertFunc);
    }

    private <RESULT extends Result<?>> Observable<RESULT> executeDelete(final RestRequest request, final Function<RESPONSE, RESULT> convertFunc) {
        return convertObservable(client.delete(getUrl(request.getPath()), request.getParameters(), request.getRequestHeaders()), convertFunc);
    }

    private String getUrl(final String path) {
        return String.format(Locale.JAPAN, "%s/%s", getContext().getBaseUrl(), path.startsWith("/") ? path.substring(1) : path);
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
