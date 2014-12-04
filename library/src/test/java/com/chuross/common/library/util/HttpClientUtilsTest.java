package com.chuross.common.library.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.chuross.common.library.http.EnclosingRequestParameter;
import com.chuross.common.library.http.HttpResponse;
import com.chuross.testcase.http.HttpRequestTestCase;
import com.chuross.testcase.http.RequestPattern;
import com.chuross.testcase.http.Response;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.List;

public class HttpClientUtilsTest extends HttpRequestTestCase {

    private static final String RESULT = "{\"hoge\": \"fuga\"}";
    private static final String ENCODING = "UTF-8";
    private static final String CONTENT_TYPE = ContentType.APPLICATION_JSON.getMimeType();

    @Test
    public void getでリクエストができる() throws Exception {
        final List<NameValuePair> parameters = Lists.newArrayList();
        parameters.add(new BasicNameValuePair("hoge", "fuga"));
        parameters.add(new BasicNameValuePair("wawa", "abibi"));

        final List<Header> requestHeaders = Lists.newArrayList();
        requestHeaders.add(new BasicHeader("testHeader", "ababa"));

        final RequestPattern pattern = new RequestPattern("/test", parameters, requestHeaders);
        final Response response = new Response(200, RESULT, ENCODING, CONTENT_TYPE, null);
        addResponse(pattern, response);

        final HttpResponse result = HttpClientUtils.get(MoreExecutors.sameThreadExecutor(), getUrl("/test"), parameters, requestHeaders, null, 1).get();
        assertThat(result.getStatus(), is(200));
        assertThat(result.getContentsAsString(ENCODING), is(RESULT));
    }

    @Test
    public void postでリクエストができる() throws Exception {
        final List<NameValuePair> parameters = Lists.newArrayList();
        parameters.add(new BasicNameValuePair("hoge", "fuga"));
        parameters.add(new BasicNameValuePair("wawa", "abibi"));

        final List<Header> requestHeaders = Lists.newArrayList();
        requestHeaders.add(new BasicHeader("testHeader", "ababa"));

        final RequestPattern pattern = new RequestPattern("/test", parameters, requestHeaders);
        final Response response = new Response(200, RESULT, ENCODING, CONTENT_TYPE, null);
        addResponse(pattern, response);

        final HttpResponse result = HttpClientUtils.post(MoreExecutors.sameThreadExecutor(), getUrl("/test"), new EnclosingRequestParameter(parameters), requestHeaders, null, 1).get();
        assertThat(result.getStatus(), is(200));
        assertThat(result.getContentsAsString(ENCODING), is(RESULT));
    }

    @Test
    public void postでjsonを送れる() throws Exception {
        final String parameter = "{\"hello\": \"world\"}";

        final List<Header> requestHeaders = Lists.newArrayList();
        requestHeaders.add(new BasicHeader("testHeader", "ababa"));

        final RequestPattern pattern = new RequestPattern("/test", parameter, requestHeaders);
        final Response response = new Response(200, RESULT, ENCODING, CONTENT_TYPE, null);
        addResponse(pattern, response);

        final HttpResponse result = HttpClientUtils.post(MoreExecutors.sameThreadExecutor(), getUrl("/test"), new EnclosingRequestParameter(parameter), requestHeaders, null, 1).get();
        assertThat(result.getStatus(), is(200));
        assertThat(result.getContentsAsString(ENCODING), is(RESULT));
    }

    @Test
    public void putでリクエストができる() throws Exception {
        final List<NameValuePair> parameters = Lists.newArrayList();
        parameters.add(new BasicNameValuePair("hoge", "fuga"));
        parameters.add(new BasicNameValuePair("wawa", "ababa"));

        final List<Header> requestHeaders = Lists.newArrayList();
        requestHeaders.add(new BasicHeader("testHeader", "ababa"));

        final RequestPattern pattern = new RequestPattern("/test", parameters, requestHeaders);
        final Response response = new Response(200, RESULT, ENCODING, CONTENT_TYPE, null);
        addResponse(pattern, response);

        final HttpResponse result = HttpClientUtils.put(MoreExecutors.sameThreadExecutor(), getUrl("/test"), new EnclosingRequestParameter(parameters), requestHeaders, null, 1).get();
        assertThat(result.getStatus(), is(200));
        assertThat(result.getContentsAsString(ENCODING), is(RESULT));
    }

    @Test
    public void putでjsonを送れる() throws Exception {
        final String parameter = "{\"hello\": \"world\"}";

        final List<Header> requestHeaders = Lists.newArrayList();
        requestHeaders.add(new BasicHeader("testHeader", "ababa"));

        final RequestPattern pattern = new RequestPattern("/test", parameter, requestHeaders);
        final Response response = new Response(200, RESULT, ENCODING, CONTENT_TYPE, null);
        addResponse(pattern, response);

        final HttpResponse result = HttpClientUtils.put(MoreExecutors.sameThreadExecutor(), getUrl("/test"), new EnclosingRequestParameter(parameter), requestHeaders, null, 1).get();
        assertThat(result.getStatus(), is(200));
        assertThat(result.getContentsAsString(ENCODING), is(RESULT));
    }

    @Test
    public void deleteでリクエストができる() throws Exception {
        final List<NameValuePair> parameters = Lists.newArrayList();
        parameters.add(new BasicNameValuePair("hoge", "fuga"));
        parameters.add(new BasicNameValuePair("wawa", "abibi"));

        final List<Header> requestHeaders = Lists.newArrayList();
        requestHeaders.add(new BasicHeader("testHeader", "ababa"));

        final RequestPattern pattern = new RequestPattern("/test", parameters, requestHeaders);
        final Response response = new Response(200, RESULT, ENCODING, CONTENT_TYPE, null);
        addResponse(pattern, response);

        final HttpResponse result = HttpClientUtils.delete(MoreExecutors.sameThreadExecutor(), getUrl("/test"), parameters, requestHeaders, null, 1).get();
        assertThat(result.getStatus(), is(200));
        assertThat(result.getContentsAsString(ENCODING), is(RESULT));
    }

}
