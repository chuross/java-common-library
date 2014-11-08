package com.chuross.common.library.util;

import com.chuross.common.library.http.EnclosingRequestParameter;
import com.chuross.common.library.http.HttpResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class HttpClientUtils {

    private HttpClientUtils() {
    }

    public static Future<HttpResponse> get(final Executor executor, final String url, final List<NameValuePair> parameters, final List<Header> requestHeaders, final RequestConfig config, final int retryCount) {
        return MethodCallUtils.callOrNull(new Callable<Future<HttpResponse>>() {
            @Override
            public Future<HttpResponse> call() throws Exception {
                URI uri = getUriWithParameter(url, parameters);
                final HttpGet request = new HttpGet(uri);
                setHeadersIfExists(request, requestHeaders);
                return execute(executor, request, config, retryCount);
            }
        });
    }

    public static Future<HttpResponse> post(final Executor executor, final String url, final EnclosingRequestParameter parameter, final List<Header> requestHeaders, final RequestConfig config, final int retryCount) {
        return MethodCallUtils.callOrNull(new Callable<Future<HttpResponse>>() {
            @Override
            public Future<HttpResponse> call() throws Exception {
                URI uri = URI.create(url);
                HttpPost request = new HttpPost(uri);
                setHeadersIfExists(request, requestHeaders);
                setEntityIfNotNull(request, parameter);
                return execute(executor, request, config, retryCount);
            }
        });
    }

    public static Future<HttpResponse> post(final Executor executor, final String url, final EnclosingRequestParameter parameter, final String uploadParameterName, final byte[] data, final List<Header> requestHeaders, final RequestConfig config, final int retryCount) {
        return MethodCallUtils.callOrNull(new Callable<Future<HttpResponse>>() {
            @Override
            public Future<HttpResponse> call() throws Exception {
                URI uri = URI.create(url);
                HttpPost request = new HttpPost(uri);
                setHeadersIfExists(request, requestHeaders);
                setEntityIfNotNull(request, parameter, uploadParameterName, data);
                return execute(executor, request, config, retryCount);
            }
        });
    }

    public static Future<HttpResponse> put(final Executor executor, final String url, final EnclosingRequestParameter parameter, final List<Header> requestHeaders, final RequestConfig config, final int retryCount) {
        return MethodCallUtils.callOrNull(new Callable<Future<HttpResponse>>() {
            @Override
            public Future<HttpResponse> call() throws Exception {
                URI uri = URI.create(url);
                HttpPut request = new HttpPut(uri);
                setHeadersIfExists(request, requestHeaders);
                setEntityIfNotNull(request, parameter);
                return execute(executor, request, config, retryCount);
            }
        });
    }

    public static Future<HttpResponse> delete(final Executor executor, final String url, final List<NameValuePair> parameters, final List<Header> requestHeaders, final RequestConfig config, final int retryCount) {
        return MethodCallUtils.callOrNull(new Callable<Future<HttpResponse>>() {
            @Override
            public Future<HttpResponse> call() throws Exception {
                URI uri = getUriWithParameter(url, parameters);
                final HttpDelete request = new HttpDelete(uri);
                setHeadersIfExists(request, requestHeaders);
                return execute(executor, request, config, retryCount);
            }
        });
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
        List<NameValuePair> nameValuePairs = parameter != null && !StringUtils.isBlank(parameter.getBody()) ? URLEncodedUtils.parse(parameter.getBody(), Charset.defaultCharset()) : new ArrayList<NameValuePair>();
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

    private static Future<HttpResponse> execute(Executor executor, final HttpUriRequest request, final RequestConfig config, int retryCount) {
        final CloseableHttpClient client = HttpClients.custom()
                .setRetryHandler(getRetryHandler(retryCount))
                .setRedirectStrategy(new DefaultRedirectStrategy() {
                    @Override
                    protected boolean isRedirectable(String method) {
                        return config != null && config.isRedirectsEnabled();
                    }
                })
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

                @Override
                public HttpResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                    try {
                        return super.get(timeout, unit);
                    } catch(InterruptedException e) {
                        abort(request);
                        throw e;
                    }
                }
            });
        } finally {
            IOUtils.closeQuietly(client);
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
            IOUtils.closeQuietly(response);
        }
    }

    private static void abort(HttpUriRequest request) {
        if(request == null) {
            return;
        }
        request.abort();
    }

    // for android
    public static String encode(List<NameValuePair> parameters, String charset) {
        StringBuilder builder = new StringBuilder();
        for(NameValuePair parameter : parameters) {
            builder.append(encode(parameter, charset));
        }
        return builder.toString().substring(1);
    }

    private static String encode(final NameValuePair parameter, final String charset) {
        return MethodCallUtils.callOrNull(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "&" + parameter.getName() + "=" + URLEncoder.encode(parameter.getValue(), charset);
            }
        });
    }

    public static Header getHeaderByName(List<Header> headers, String name) {
        for(Header header : headers) {
            if(header.getName().equals(name)) {
                return header;
            }
        }
        return null;
    }

    public static HeaderElement getHeaderElementByName(HeaderElement[] elements, String name) {
        for(HeaderElement element : elements) {
            if(element.getName().equals(name)) {
                return element;
            }
        }
        return null;
    }
}
