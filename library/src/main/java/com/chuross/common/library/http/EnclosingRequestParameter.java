package com.chuross.common.library.http;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public class EnclosingRequestParameter {

    private String body;

    public EnclosingRequestParameter(List<NameValuePair> parameters){
        this(parameters, Charset.forName("UTF-8"));
    }

    public EnclosingRequestParameter(List<NameValuePair> parameters, Charset charset) {
        InputStream inputStream = null;
        try {
            inputStream = parameters != null ? new UrlEncodedFormEntity(parameters, charset).getContent() : null;
            body = IOUtils.toString(inputStream);
        } catch(Exception e) {
            throw new IllegalArgumentException();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public EnclosingRequestParameter(String body){
        this.body = body;
    }

    public String getBody() {
        return body;
    }

}
