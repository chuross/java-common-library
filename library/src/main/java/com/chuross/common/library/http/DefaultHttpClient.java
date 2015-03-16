package com.chuross.common.library.http;

import com.chuross.common.library.util.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.net.MediaType;
import org.apache.commons.lang3.StringUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;

public class DefaultHttpClient implements HttpClient<DefaultResponse> {

    private DefaultRequestConfig config;

    @Override
    public Observable<DefaultResponse> get(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, HeaderElement> requestHeaders) {
        final String parameter = HttpClientUtils.toQueryString(parameters);
        return execute(false, "GET", url, parameter, requestHeaders, getConfig());
    }

    @Override
    public Observable<DefaultResponse> post(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, HeaderElement> requestHeaders) {
        final String parameter = HttpClientUtils.toQueryString(parameters);
        return execute(true, "POST", url, parameter, requestHeaders, getConfig());
    }

    @Override
    public Observable<DefaultResponse> post(final String url, final String requestBody, final ListMultimap<String, HeaderElement> requestHeaders) {
        return execute(true, "POST", url, requestBody, requestHeaders, getConfig());
    }

    @Override
    public Observable<DefaultResponse> put(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, HeaderElement> requestHeaders) {
        final String parameter = HttpClientUtils.toQueryString(parameters);
        return execute(true, "PUT", url, parameter, requestHeaders, getConfig());
    }

    @Override
    public Observable<DefaultResponse> put(final String url, final String requestBody, final ListMultimap<String, HeaderElement> requestHeaders) {
        return execute(true, "PUT", url, requestBody, requestHeaders, getConfig());
    }

    @Override
    public Observable<DefaultResponse> delete(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, HeaderElement> requestHeaders) {
        final String parameter = HttpClientUtils.toQueryString(parameters);
        return execute(false, "DELETE", url, parameter, requestHeaders, getConfig());
    }

    private DefaultRequestConfig getConfig() {
        return config != null ? config : new DefaultRequestConfig();
    }

    private static Observable<DefaultResponse> execute(final boolean doInput, final String method, final String url, final String parameter, final ListMultimap<String, HeaderElement> requestHeaders, final DefaultRequestConfig config) {
        final String targetUrl = getTargetUrl(doInput, url, parameter);
        final HttpURLConnection connection = openConnection(doInput, method, targetUrl, parameter, requestHeaders, config);
        return Observable.create(new Observable.OnSubscribe<DefaultResponse>() {
            @Override
            public void call(final Subscriber<? super DefaultResponse> subscriber) {
                onSubscribe(connection, subscriber);
            }
        });
    }

    private static void onSubscribe(final HttpURLConnection connection, final Subscriber<? super DefaultResponse> subscriber) {
        try {
            connect(connection);
            subscriber.add(Subscriptions.create(new Action0() {
                @Override
                public void call() {
                    disconnect(connection);
                }
            }));
            subscriber.onNext(getResponse(connection));
            subscriber.onCompleted();
        } catch(final Exception e) {
            subscriber.onError(e);
            subscriber.onCompleted();
        }
    }

    private static DefaultResponse getResponse(final HttpURLConnection connection) throws IOException {
        try {
            final InputStream inputStream = connection.getInputStream();
            final byte[] data = IOUtils.toByteArray(inputStream);
            inputStream.close();
            disconnect(connection);
            return new DefaultResponse(getStatusCode(connection), getResponseHeaders(connection), data);
        } catch(final Exception e) {
            return new DefaultResponse(getStatusCode(connection), getResponseHeaders(connection), e);
        }
    }

    private static int getStatusCode(final HttpURLConnection connection) throws IOException {
        return connection.getResponseCode();
    }

    private static ListMultimap<String, HeaderElement> getResponseHeaders(final HttpURLConnection connection) {
        final ListMultimap<String, HeaderElement> multiMap = ArrayListMultimap.create();
        CollectionUtils.foreach(connection.getHeaderFields(), new Procedure<String, List<String>>() {
            @Override
            public void process(final String key, final List<String> values) {
                for(final String value : values) {
                    multiMap.put(key, HeaderElement.of(value));
                }
            }
        });
        return multiMap;
    }

    private static String getTargetUrl(final boolean doInput, final String url, final String parameter) {
        if(doInput) {
            return url;
        }
        return StringUtils.isBlank(parameter) ? url : url + "?" + parameter;
    }

    private static HttpURLConnection openConnection(final boolean doInput, final String method, final String url, final String parameter, final ListMultimap<String, HeaderElement> requestHeaders, final DefaultRequestConfig config) {
        return MethodCallUtils.callOrNull(new Callable<HttpURLConnection>() {
            @Override
            public HttpURLConnection call() throws Exception {
                return openConnectionInCallable(doInput, method, url, parameter, requestHeaders, config);
            }
        });
    }

    private static HttpURLConnection openConnectionInCallable(final boolean doInput, final String method, final String url, final String parameter, final ListMultimap<String, HeaderElement> requestHeaders, final DefaultRequestConfig config) throws Exception {
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(config.getConnectionTimeout());
        connection.setReadTimeout(config.getReadTimeout());
        connection.setInstanceFollowRedirects(config.isAllowRedirect());
        setRequestParameters(connection, requestHeaders);
        if(doInput) {
            setParameter(connection, parameter);
        }
        return connection;
    }

    private static void setRequestParameters(final HttpURLConnection connection, final ListMultimap<String, HeaderElement> requestParameters) {
        CollectionUtils.foreach(requestParameters, new Procedure<String, HeaderElement>() {
            @Override
            public void process(final String key, final HeaderElement value) {
                connection.addRequestProperty(key, value.toString());
            }
        });
    }

    private static void setParameter(final HttpURLConnection connection, final String parameter) throws IOException {
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if(connection.getRequestProperty("Content-Type") == null) {
            connection.setRequestProperty("Content-Type", MediaType.FORM_DATA.toString());
        }
        org.apache.commons.io.IOUtils.write(parameter, connection.getOutputStream());
    }

    private static void connect(final HttpURLConnection connection) throws IOException {
        if(connection != null) {
            connection.connect();
        }
    }

    private static void disconnect(final HttpURLConnection connection) {
        if(connection != null) {
            connection.disconnect();
        }
    }

    public void setRequestConfig(final DefaultRequestConfig config) {
        this.config = config;
    }
}
