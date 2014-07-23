package com.chuross.common.library.util;

import com.chuross.common.library.http.HttpResponse;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
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
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public final class HttpClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

    public static Future<HttpResponse> get(String url, List<NameValuePair> parameters, List<Header> requestHeaders, RequestConfig config, int retryCount) {
        try {
            URI uri = new URIBuilder(url).addParameters(parameters).build();
            final HttpGet request = new HttpGet(uri);
            request.setHeaders(requestHeaders.toArray(new Header[requestHeaders.size()]));
            return execute(request, config, retryCount);
        } catch(Exception e) {
            LOGGER.error("get request failed.", e);
            return null;
        }
    }

    public static Future<HttpResponse> post(String url, List<NameValuePair> parameters, List<Header> requestHeaders, RequestConfig config, int retryCount) {
        try {
            URI uri = URI.create(url);
            HttpPost request = new HttpPost(uri);
            request.setHeaders(requestHeaders.toArray(new Header[requestHeaders.size()]));
            request.setEntity(new UrlEncodedFormEntity(parameters));
            return execute(request, config, retryCount);
        } catch(Exception e) {
            LOGGER.error("post request failed.", e);
            return null;
        }
    }

    public static Future<HttpResponse> post(String url, String body, List<Header> requestHeaders, RequestConfig config, int retryCount) {
        try {
            URI uri = URI.create(url);
            HttpPost request = new HttpPost(uri);
            request.setHeaders(requestHeaders.toArray(new Header[requestHeaders.size()]));
            request.setEntity(new StringEntity(body));
            return execute(request, config, retryCount);
        } catch(Exception e) {
            LOGGER.error("post request failed.", e);
            return null;
        }
    }

    public static Future<HttpResponse> put(String url, List<NameValuePair> parameters, List<Header> requestHeaders, RequestConfig config, int retryCount) {
        try {
            URI uri = URI.create(url);
            HttpPut request = new HttpPut(uri);
            request.setHeaders(requestHeaders.toArray(new Header[requestHeaders.size()]));
            request.setEntity(new UrlEncodedFormEntity(parameters));
            return execute(request, config, retryCount);
        } catch(Exception e) {
            LOGGER.error("put request failed.", e);
            return null;
        }
    }

    public static Future<HttpResponse> put(String url, String body, List<Header> requestHeaders, RequestConfig config, int retryCount) {
        try {
            URI uri = URI.create(url);
            HttpPut request = new HttpPut(uri);
            request.setHeaders(requestHeaders.toArray(new Header[requestHeaders.size()]));
            request.setEntity(new StringEntity(body));
            return execute(request, config, retryCount);
        } catch(Exception e) {
            LOGGER.error("put request failed.", e);
            return null;
        }
    }

    public static Future<HttpResponse> delete(String url, List<NameValuePair> parameters, List<Header> requestHeaders, RequestConfig config, int retryCount) {
        try {
            URI uri = new URIBuilder(url).addParameters(parameters).build();
            final HttpDelete request = new HttpDelete(uri);
            request.setHeaders(requestHeaders.toArray(new Header[requestHeaders.size()]));
            return execute(request, config, retryCount);
        } catch(Exception e) {
            LOGGER.error("delete request failed.", e);
            return null;
        }
    }

    private static Future<HttpResponse> execute(final HttpUriRequest request, RequestConfig config, int retryCount) {
        final CloseableHttpClient client = HttpClients.custom()
                .setRetryHandler(getRetryHandler(retryCount))
                .setDefaultRequestConfig(config)
                .build();
        try {
            return new FutureTask<HttpResponse>(new Callable<HttpResponse>() {
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
            };
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

    public static void close(Closeable closeable) {
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

}
