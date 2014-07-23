package com.chuross.common.library.http;

import org.apache.http.Header;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HttpResponse {

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

    public String getContentsAsString(String charset) {
        return new String(contents);
    }

}
