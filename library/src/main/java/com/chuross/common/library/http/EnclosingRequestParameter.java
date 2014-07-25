package com.chuross.common.library.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.Charset;
import java.util.List;

public class EnclosingRequestParameter {

    private String body;

    public EnclosingRequestParameter(List<NameValuePair> parameters){
        this(parameters, Charset.forName("UTF-8"));
    }

    public EnclosingRequestParameter(List<NameValuePair> parameters, Charset charset) {
        body = URLEncodedUtils.format(parameters, charset);
    }

    public EnclosingRequestParameter(String body){
        this.body = body;
    }

    public String getBody() {
        return body;
    }

}
