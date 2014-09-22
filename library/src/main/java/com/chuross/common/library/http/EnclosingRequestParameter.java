package com.chuross.common.library.http;

import com.chuross.common.library.util.HttpClientUtils;
import org.apache.http.NameValuePair;

import java.nio.charset.Charset;
import java.util.List;

public class EnclosingRequestParameter {

    private String body;

    public EnclosingRequestParameter(List<NameValuePair> parameters) {
        this(parameters, Charset.forName("UTF-8"));
    }

    public EnclosingRequestParameter(List<NameValuePair> parameters, Charset charset) {
        body = HttpClientUtils.encode(parameters, charset.name());
    }

    public EnclosingRequestParameter(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}
