package com.chuross.common.library.test;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
import java.net.InetSocketAddress;
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
        RequestPattern pattern = new RequestPattern(target, getParameters(request), getRequestHeader(request));
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
        while(names.hasMoreElements()) {
            String name = names.nextElement();
            if(name.equals("Accept-Encoding") || name.equals("Connection") || name.equals("Host") || name.equals("User-Agent")) {
                continue;
            }
            requestHeaders.addAll(getRequestHeader(request, name));
        }
        return requestHeaders;
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

    public class RequestPattern {

        private String path;
        private List<NameValuePair> parameters;
        private List<Header> requestHeaders;

        public RequestPattern(String path, List<NameValuePair> parameters, List<Header> requestHeaders) {
            this.path = path;
            this.parameters = parameters;
            this.requestHeaders = requestHeaders;
            Collections.sort(this.parameters, new Comparator<NameValuePair>() {
                @Override
                public int compare(NameValuePair o1, NameValuePair o2) {
                    int compare = o1.getName().compareTo(o2.getName());
                    if(compare != 0) {
                        return compare;
                    }
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
            Collections.sort(this.requestHeaders, new Comparator<Header>() {
                @Override
                public int compare(Header o1, Header o2) {
                    int compare = o1.getName().compareTo(o2.getName());
                    if(compare != 0) {
                        return compare;
                    }
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
        }

        public String getPath() {
            return path;
        }

        public List<NameValuePair> getParameters() {
            return parameters;
        }

        public List<Header> getRequestHeaders() {
            return requestHeaders;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof  RequestPattern)) {
                return false;
            }
            RequestPattern pattern = (RequestPattern) obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(path, pattern.getPath());

            List<NameValuePair> targetParameters = pattern.getParameters();
            builder.append(parameters.size(), targetParameters.size());
            for(int i = 0; i < parameters.size(); i++) {
                builder.append(parameters.get(i).getName(), targetParameters.get(i).getName());
                builder.append(parameters.get(i).getValue(), targetParameters.get(i).getValue());
            }

            List<Header> targetRequestHeaders = pattern.getRequestHeaders();
            builder.append(requestHeaders.size(), targetRequestHeaders.size());
            for(int i = 0; i < requestHeaders.size(); i++) {
                builder.append(requestHeaders.get(i).getName(), targetRequestHeaders.get(i).getName());
                builder.append(requestHeaders.get(i).getValue(), targetRequestHeaders.get(i).getValue());
            }
            return builder.build();
        }

        @Override
        public int hashCode() {
            HashCodeBuilder builder = new HashCodeBuilder();
            builder.append(path);
            for(NameValuePair parameter : parameters) {
                builder.append(parameter.getName());
                builder.append(parameter.getValue());
            }
            for(Header header : requestHeaders) {
                builder.append(header.getName());
                builder.append(header.getValue());
            }
            return builder.build();
        }
    }

    public class Response {

        private int status;
        private String body;
        private String encoding;
        private String contentType;
        private List<Header> responseHeaders;

        public Response(int status, String body, String encoding, String contentType, List<Header> responseHeaders) {
            this.status = status;
            this.body = body;
            this.encoding = encoding;
            this.contentType = contentType;
            this.responseHeaders = responseHeaders;
        }

        public int getStatus() {
            return status;
        }

        public String getBody() {
            return body;
        }

        public String getEncoding() {
            return encoding;
        }

        public String getContentType() {
            return contentType;
        }

        public List<Header> getResponseheaders() {
            return responseHeaders;
        }

    }

}
