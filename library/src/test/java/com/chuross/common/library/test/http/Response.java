package com.chuross.common.library.test.http;

import org.apache.http.Header;

import java.util.List;

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