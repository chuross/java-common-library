package com.chuross.common.library.util;

import com.chuross.common.library.http.EnclosingRequestParameter;
import com.chuross.common.library.http.HttpResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public final class HttpClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

    public static Future<HttpResponse> get(Executor executor, String url, List<NameValuePair> parameters, List<Header> requestHeaders, RequestConfig config, int retryCount) {
        try {
            URI uri = getUriWithParameter(url, parameters);
            final HttpGet request = new HttpGet(uri);
            setHeadersIfExists(request, requestHeaders);
            return execute(executor, request, config, retryCount);
        } catch(Exception e) {
            LOGGER.error("get request failed.", e);
            return null;
        }
    }

    public static Future<HttpResponse> post(Executor executor, String url, EnclosingRequestParameter parameter, List<Header> requestHeaders, RequestConfig config, int retryCount) {
        try {
            URI uri = URI.create(url);
            HttpPost request = new HttpPost(uri);
            setHeadersIfExists(request, requestHeaders);
            setEntityIfNotNull(request, parameter);
            return execute(executor, request, config, retryCount);
        } catch(Exception e) {
            LOGGER.error("post request failed.", e);
            return null;
        }
    }

    public static Future<HttpResponse> post(Executor executor, String url, EnclosingRequestParameter parameter, String uploadParameterName, byte[] data, List<Header> requestHeaders, RequestConfig config, int retryCount) {
        try {
            URI uri = URI.create(url);
            HttpPost request = new HttpPost(uri);
            setHeadersIfExists(request, requestHeaders);
            setEntityIfNotNull(request, parameter, uploadParameterName, data);
            return execute(executor, request, config, retryCount);
        } catch(Exception e) {
            LOGGER.error("post request failed.", e);
            return null;
        }
    }

    public static Future<HttpResponse> put(Executor executor, String url, EnclosingRequestParameter parameter, List<Header> requestHeaders, RequestConfig config, int retryCount) {
        try {
            URI uri = URI.create(url);
            HttpPut request = new HttpPut(uri);
            setHeadersIfExists(request, requestHeaders);
            setEntityIfNotNull(request, parameter);
            return execute(executor, request, config, retryCount);
        } catch(Exception e) {
            LOGGER.error("put request failed.", e);
            return null;
        }
    }

    public static Future<HttpResponse> delete(Executor executor, String url, List<NameValuePair> parameters, List<Header> requestHeaders, RequestConfig config, int retryCount) {
        try {
            URI uri = getUriWithParameter(url, parameters);
            final HttpDelete request = new HttpDelete(uri);
            setHeadersIfExists(request, requestHeaders);
            return execute(executor, request, config, retryCount);
        } catch(Exception e) {
            LOGGER.error("delete request failed.", e);
            return null;
        }
    }

    private static URI getUriWithParameter(String url, List<NameValuePair> parameters) throws Exception {
        if(parameters == null || parameters.size() <= 0) {
            return URI.create(url);
        }
        return new URIBuilder(url).addParameters(parameters).build();
    }

    private static void setEntityIfNotNull(HttpEntityEnclosingRequest request, EnclosingRequestParameter parameter) throws Exception {
        if(parameter == null || StringUtils.isBlank(parameter.getBody())) {
            return;
        }
        request.setEntity(new StringEntity(parameter.getBody()));
    }

    private static void setEntityIfNotNull(HttpEntityEnclosingRequest request, EnclosingRequestParameter parameter, String uploadParameterName, byte[] data) throws Exception {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        if(!StringUtils.isBlank(uploadParameterName) && data != null && data.length > 0) {
            builder.addBinaryBody(uploadParameterName, data);
        }
        List<NameValuePair> nameValuePairs = parameter != null && parameter.getParameters() != null ? parameter.getParameters() : new ArrayList<NameValuePair>();
        if(nameValuePairs.size() > 0) {
            for(NameValuePair nameValuePair : nameValuePairs) {
                builder.addTextBody(nameValuePair.getName(), nameValuePair.getValue());
            }
        }
        request.setEntity(builder.build());
    }

    private static void setHeadersIfExists(HttpUriRequest request, List<Header> requestHeaders) {
        if(requestHeaders == null || requestHeaders.size() <= 0) {
            return;
        }
        request.setHeaders(requestHeaders.toArray(new Header[requestHeaders.size()]));
    }

    private static Future<HttpResponse> execute(Executor executor, final HttpUriRequest request, RequestConfig config, int retryCount) {
        final CloseableHttpClient client = HttpClients.custom()
                .setRetryHandler(getRetryHandler(retryCount))
                .setDefaultRequestConfig(config)
                .build();
        try {
            return FutureUtils.executeOrNull(executor, new FutureTask<HttpResponse>(new Callable<HttpResponse>() {
                @Override
                public HttpResponse call() throws Exception {
                    return execute(client, request);
                }
            }) {
                @Override
                public boolean cancel(final boolean mayInterruptIfRunning) {
                    abort(request);
                    return super.cancel(mayInterruptIfRunning);
                }
            });
        } finally {
            close(client);
        }
    }

    private static HttpRequestRetryHandler getRetryHandler(final int retryCount) {
        return new DefaultHttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(final IOException exception, final int executionCount, final HttpContext context) {
                if(retryCount >= executionCount) {
                    return false;
                }
                if(exception instanceof InterruptedIOException) {
                    return false;
                }
                if(exception instanceof UnknownHostException) {
                    return false;
                }
                if(exception instanceof ConnectTimeoutException) {
                    return true;
                }
                if(exception instanceof SocketTimeoutException) {
                    return true;
                }
                return super.retryRequest(exception, executionCount, context);
            }
        };
    }

    private static HttpResponse execute(CloseableHttpClient client, HttpUriRequest request) throws Exception {
        CloseableHttpResponse response = client.execute(request);
        try {
            return new HttpResponse(response);
        } finally {
            close(response);
        }
    }

    private static void close(Closeable closeable) {
        if(closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch(Exception e) {
        }
    }

    private static void abort(HttpUriRequest request) {
        if(request == null) {
            return;
        }
        request.abort();
    }

    public static String getParameterString(List<NameValuePair> nameValuePairs) {
        StringBuilder builder = new StringBuilder();
        for(NameValuePair nameValuePair : nameValuePairs) {
            builder.append(String.format("&%s=%s", nameValuePair.getName(), nameValuePair.getValue()));
        }
        return builder.toString().substring(1);
    }

}
