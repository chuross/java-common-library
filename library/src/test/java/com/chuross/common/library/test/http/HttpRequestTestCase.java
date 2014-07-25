package com.chuross.common.library.test.http;

import com.chuross.common.library.util.IOUtils;
import com.google.common.collect.Lists;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.After;
import org.junit.Before;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.*;

public class HttpRequestTestCase {

    public static final String HOST = "localhost";
    public static final int PORT = 3000;
    public static final String URL = String.format("http://%s:%d", HOST, PORT);
    private Server server;
    private Map<RequestPattern, Response> responseMap = new HashMap<RequestPattern, Response>();

    @Before
    public void beforeServerTest() throws Exception {
        InetSocketAddress socketAddress = new InetSocketAddress(HOST, PORT);
        server = new Server(socketAddress);
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                onHandle(target, baseRequest, request, response);
            }
        });
        server.start();
    }

    private void onHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        InputStream inputStream = request.getInputStream();
        RequestPattern pattern =  method.equals("POST") || method.equals("PUT") ? new RequestPattern(target, IOUtils.toString(inputStream, Charset.defaultCharset()), getRequestHeader(request)) : new RequestPattern(target, getParameters(request), getRequestHeader(request));
        org.apache.commons.io.IOUtils.closeQuietly(inputStream);
        if(!responseMap.containsKey(pattern)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            baseRequest.setHandled(true);
            return;
        }
        Response responseValue = responseMap.get(pattern);
        setResponseHeadersIfNotNull(response, responseValue.getResponseheaders());
        response.setStatus(responseValue.getStatus());
        response.setContentType(responseValue.getContentType());
        response.setCharacterEncoding(responseValue.getEncoding());
        response.getWriter().write(responseValue.getBody());
        baseRequest.setHandled(true);
    }

    private List<NameValuePair> getParameters(HttpServletRequest request) {
        List<NameValuePair> parameters = Lists.newArrayList();
        Enumeration<String> names =  request.getParameterNames();
        while(names.hasMoreElements()) {
            parameters.addAll(getParameters(request, names.nextElement()));
        }
        return parameters;
    }

    private List<NameValuePair> getParameters(HttpServletRequest request, String name) {
        List<NameValuePair> parameters = Lists.newArrayList();
        String[] values = request.getParameterValues(name);
        for(String value : values) {
            parameters.add(new BasicNameValuePair(name, value));
        }
        return parameters;
    }

    private List<Header> getRequestHeader(HttpServletRequest request) {
        List<Header> requestHeaders = Lists.newArrayList();
        Enumeration<String> names = request.getHeaderNames();
        List<String> originalHeaderNames = getOriginalRequestHeaderNames();
        while(names.hasMoreElements()) {
            String name = names.nextElement();
            if(!originalHeaderNames.contains(name)) {
                continue;
            }
            requestHeaders.addAll(getRequestHeader(request, name));
        }
        return requestHeaders;
    }
    private List<String> getOriginalRequestHeaderNames() {
        List<String> requestHeaderNames = Lists.newArrayList();
        for(RequestPattern pattern : responseMap.keySet()) {
            for(Header header : pattern.getRequestHeaders()) {
                requestHeaderNames.add(header.getName());
            }
        }
        return requestHeaderNames;
    }

    private List<Header> getRequestHeader(HttpServletRequest request, String name) {
        List<Header> requestHeaders = Lists.newArrayList();
        Enumeration<String> values = request.getHeaders(name);
        while(values.hasMoreElements()) {
            requestHeaders.add(new BasicHeader(name, values.nextElement()));
        }
        return requestHeaders;
    }

    private void setResponseHeadersIfNotNull(HttpServletResponse response, List<Header> responseHeaders) {
        if(responseHeaders == null || responseHeaders.size() <= 0) {
            return;
        }
        for(Header header : responseHeaders) {
            response.addHeader(header.getName(), header.getValue());
        }
    }

    @After
    public void afterServerTest() throws Exception {
        server.stop();
        server.destroy();
    }

    public void addResponse(RequestPattern pattern, Response response) {
        if(pattern == null || response == null) {
            throw new IllegalArgumentException();
        }
        responseMap.put(pattern, response);
    }

    public String getUrl(String path) {
        return String.format("%s/%s", URL, path.startsWith("/") ? path.substring(1) : path);
    }


}
