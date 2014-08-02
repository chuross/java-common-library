package com.chuross.common.library.http;

import org.apache.http.Header;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HttpResponse {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpResponse.class);

    private int status;
    private List<Header> headers;
    private byte[] contents;

    public HttpResponse(org.apache.http.HttpResponse response) throws IllegalArgumentException, IOException {
        if(response == null) {
            throw new IllegalArgumentException("Response is null.");
        }
        this.status = response.getStatusLine().getStatusCode();
        this.headers = Arrays.asList(response.getAllHeaders());
        this.contents = EntityUtils.toByteArray(response.getEntity());
    }

    public int getStatus() {
        return status;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public byte[] getContents() {
        return contents;
    }

    public String getContentsAsString() {
        return getContentsAsString("UTF-8");
    }

    public String getContentsAsString(String charset) {
        try {
            return new String(contents, charset);
        } catch(Exception e) {
            LOGGER.error("getContentsAsString error.", e);
            return null;
        }
    }

}
