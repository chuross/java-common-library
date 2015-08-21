package com.chuross.common.library.http;

import com.chuross.common.library.util.CollectionUtils;
import com.chuross.common.library.util.HttpClientUtils;
import com.chuross.common.library.util.Procedure;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.apache.commons.lang3.StringUtils;
import rx.Observable;
import rx.Subscriber;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpClient implements HttpClient<DefaultResponse> {

    private com.squareup.okhttp.OkHttpClient client = new com.squareup.okhttp.OkHttpClient();
    private OkHttpRequestConfig requestConfig;

    @Override
    public Observable<DefaultResponse> get(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, HeaderElement> requestHeaders) {
        return execute(Method.GET, url, HttpClientUtils.toQueryString(parameters), requestHeaders);
    }

    @Override
    public Observable<DefaultResponse> post(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, HeaderElement> requestHeaders) {
        return execute(Method.POST, url, HttpClientUtils.toQueryString(parameters), requestHeaders);
    }

    @Override
    public Observable<DefaultResponse> post(final String url, final String requestBody, final ListMultimap<String, HeaderElement> requestHeaders) {
        return execute(Method.POST, url, requestBody, requestHeaders);
    }

    @Override
    public Observable<DefaultResponse> put(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, HeaderElement> requestHeaders) {
        return execute(Method.PUT, url, HttpClientUtils.toQueryString(parameters), requestHeaders);
    }

    @Override
    public Observable<DefaultResponse> put(final String url, final String requestBody, final ListMultimap<String, HeaderElement> requestHeaders) {
        return execute(Method.PUT, url, requestBody, requestHeaders);
    }

    @Override
    public Observable<DefaultResponse> delete(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, HeaderElement> requestHeaders) {
        return execute(Method.DELETE, url, HttpClientUtils.toQueryString(parameters), requestHeaders);
    }

    private Observable<DefaultResponse> execute(final Method method, final String url, final String parameter, final ListMultimap<String, HeaderElement> requestHeaders) {
        return Observable.create(new Observable.OnSubscribe<DefaultResponse>() {
            @Override
            public void call(final Subscriber<? super DefaultResponse> subscriber) {
                execute(method, url, parameter, requestHeaders, subscriber);
            }
        });
    }

    private void execute(final Method method, final String url, final String parameter, final ListMultimap<String, HeaderElement> requestHeaders, final Subscriber<? super DefaultResponse> subscriber) {
        try {
            final Request.Builder builder = new Request.Builder();
            builder.url(getUrl(method, url, parameter));
            setMethod(builder, method, parameter);
            setRequestHeaders(builder, requestHeaders);
            call(builder.build(), subscriber);
        } catch(final Exception e) {
            subscriber.onError(e);
        }
    }

    private static String getUrl(final Method method, final String url, final String parameter) {
        if(method.equals(Method.POST) || method.equals(Method.PUT) || StringUtils.isBlank(parameter)) {
            return url;
        }
        return url + "?" + parameter;
    }

    private static void setMethod(final Request.Builder builder, final Method method, final String parameter) {
        if(method.equals(Method.GET)) {
            builder.get();
        } else if(method.equals(Method.POST)) {
            builder.post(RequestBody.create(MediaType.parse(com.google.common.net.MediaType.FORM_DATA.toString()), parameter));
        } else if(method.equals(Method.PUT)) {
            builder.put(RequestBody.create(MediaType.parse(com.google.common.net.MediaType.FORM_DATA.toString()), parameter));
        } else if(method.equals(Method.DELETE)) {
            builder.delete();
        }
    }

    private static void setRequestHeaders(final Request.Builder builder, final ListMultimap<String, HeaderElement> requestHeaders) {
        CollectionUtils.foreach(requestHeaders, new Procedure<String, HeaderElement>() {
            @Override
            public void process(final String key, final HeaderElement value) {
                builder.addHeader(key, value.toString());
            }
        });
    }

    private void call(final Request request, final Subscriber<? super DefaultResponse> subscriber) throws IOException {
        final OkHttpRequestConfig requestConfig = getRequestConfig();
        client.setConnectTimeout(requestConfig.getConnectionTimeout(), TimeUnit.MILLISECONDS);
        client.setReadTimeout(requestConfig.getReadTimeout(), TimeUnit.MILLISECONDS);
        client.setWriteTimeout(requestConfig.getWriteTimeout(), TimeUnit.MILLISECONDS);
        client.setFollowRedirects(requestConfig.isFollowRedirects());
        final com.squareup.okhttp.Response response = client.newCall(request).execute();
        subscriber.onNext(new DefaultResponse(response.code(), toResponseHeaders(response.headers()), response.body().bytes()));
        subscriber.onCompleted();
    }

    private static ListMultimap<String, HeaderElement> toResponseHeaders(final Headers headers) {
        final ListMultimap<String, HeaderElement> responseHeaders = ArrayListMultimap.create();
        for(final Map.Entry<String, List<String>> entry : headers.toMultimap().entrySet()) {
            for(final String value : entry.getValue()) {
                responseHeaders.put(entry.getKey(), HeaderElement.of(value));
            }
        }
        return responseHeaders;
    }

    private OkHttpRequestConfig getRequestConfig() {
        return requestConfig != null ? requestConfig : new OkHttpRequestConfig.Builder().build();
    }

    public void setRequestConfig(final OkHttpRequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }
}
