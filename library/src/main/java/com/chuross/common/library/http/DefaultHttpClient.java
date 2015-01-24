package com.chuross.common.library.http;

import com.chuross.common.library.util.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.net.MediaType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public class DefaultHttpClient implements HttpClient<DefaultResponse, DefaultRequestConfig> {

    @Override
    public RunnableFuture<DefaultResponse> get(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, Object> requestHeaders, final DefaultRequestConfig config) {
        final String parameter = HttpClientUtils.toQueryString(parameters);
        return execute(false, "GET", url, parameter, requestHeaders, config);
    }

    @Override
    public RunnableFuture<DefaultResponse> post(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, Object> requestHeaders, final DefaultRequestConfig config) {
        final String parameter = HttpClientUtils.toQueryString(parameters);
        return execute(false, "POST", url, parameter, requestHeaders, config);
    }

    @Override
    public RunnableFuture<DefaultResponse> post(final String url, final String requestBody, final ListMultimap<String, Object> requestHeaders, final DefaultRequestConfig config) {
        return execute(false, "POST", url, requestBody, requestHeaders, config);
    }

    @Override
    public RunnableFuture<DefaultResponse> put(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, Object> requestHeaders, final DefaultRequestConfig config) {
        final String parameter = HttpClientUtils.toQueryString(parameters);
        return execute(false, "PUT", url, parameter, requestHeaders, config);
    }

    @Override
    public RunnableFuture<DefaultResponse> put(final String url, final String requestBody, final ListMultimap<String, Object> requestHeaders, final DefaultRequestConfig config) {
        return execute(false, "PUT", url, requestBody, requestHeaders, config);
    }

    @Override
    public RunnableFuture<DefaultResponse> delete(final String url, final ListMultimap<String, Object> parameters, final ListMultimap<String, Object> requestHeaders, final DefaultRequestConfig config) {
        final String parameter = HttpClientUtils.toQueryString(parameters);
        return execute(false, "DELETE", url, parameter, requestHeaders, config);
    }

    private static RunnableFuture<DefaultResponse> execute(final boolean doInput, final String method, final String url, final String parameter, final ListMultimap<String, Object> requestHeaders, final DefaultRequestConfig config) {
        final String targetUrl = getTargetUrl(doInput, url, parameter);
        final HttpURLConnection connection = openConnection(doInput, method, targetUrl, parameter, requestHeaders, config);
        return new FutureTask<DefaultResponse>(new Callable<DefaultResponse>() {
            @Override
            public DefaultResponse call() throws Exception {
                connect(connection);
                return getResponse(connection);
            }
        }) {
            @Override
            public boolean cancel(final boolean mayInterruptIfRunning) {
                disconnect(connection);
                return super.cancel(mayInterruptIfRunning);
            }
        };
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

    private static ListMultimap<String, Object> getResponseHeaders(final HttpURLConnection connection) {
        final ListMultimap<String, Object> multimap = ArrayListMultimap.create();
        CollectionUtils.foreach(connection.getHeaderFields(), new Procedure<String, List<String>>() {
            @Override
            public void process(final String key, final List<String> values) {
                for(final String value : values) {
                    multimap.put(key, value);
                }
            }
        });
        return multimap;
    }

    private static String getTargetUrl(final boolean doInput, final String url, final String parameter) {
        if(doInput) {
            return url;
        }
        return StringUtils.isBlank(parameter) ? url : url + "?" + parameter;
    }

    private static HttpURLConnection openConnection(final boolean doInput, final String method, final String url, final String parameter, final ListMultimap<String, Object> requestHeaders, final DefaultRequestConfig config) {
        return MethodCallUtils.callOrNull(new Callable<HttpURLConnection>() {
            @Override
            public HttpURLConnection call() throws Exception {
                return openConnectionInCallable(doInput, method, url, parameter, requestHeaders, config);
            }
        });
    }

    private static HttpURLConnection openConnectionInCallable(final boolean doInput, final String method, final String url, final String parameter, final ListMultimap<String, Object> requestHeaders, final DefaultRequestConfig config) throws Exception {
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setDoInput(doInput);
        connection.setConnectTimeout(config.getConnectionTimeout());
        connection.setReadTimeout(config.getReadTimeout());
        connection.setAllowUserInteraction(config.isAllowRedirect());
        setRequestParameters(connection, requestHeaders);
        if(doInput) {
            setParameter(connection, parameter);
        }
        return connection;
    }

    private static void setRequestParameters(final HttpURLConnection connection, final ListMultimap<String, Object> requestParameters) {
        CollectionUtils.foreach(requestParameters, new Procedure<String, Object>() {
            @Override
            public void process(final String key, final Object value) {
                connection.addRequestProperty(key, value.toString());
            }
        });
    }

    private static void setParameter(final HttpURLConnection connection, final String parameter) throws IOException {
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
}
