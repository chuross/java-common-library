package com.chuross.common.library.util;

import com.chuross.common.library.http.DefaultHttpClient;
import com.chuross.common.library.http.DefaultResponse;
import com.chuross.testcase.http.HttpRequestTestCase;
import com.chuross.testcase.http.RequestPattern;
import com.chuross.testcase.http.Response;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.net.MediaType;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultHttpClientTest extends HttpRequestTestCase {

    private static final String RESULT = "{\"hoge\": \"fuga\"}";
    private static final MediaType CONTENT_TYPE = MediaType.JSON_UTF_8;
    private DefaultHttpClient client;

    @Before
    public void before() {
        client = new DefaultHttpClient();
    }

    @Test
    public void getでリクエストができる() throws Exception {
        final ListMultimap<String, Object> parameters = ArrayListMultimap.create();
        parameters.put("hoge", "fuga");
        parameters.put("wawa", "abibi");
        final ListMultimap<String, Object> requestHeaders = ArrayListMultimap.create();
        requestHeaders.put("testHeader", "ababa");
        final RequestPattern pattern = new RequestPattern("/test", parameters, requestHeaders);
        final Response response = new Response(200, RESULT, null, CONTENT_TYPE);
        putResponse(pattern, response);
        final DefaultResponse result = client.get(getUrl("/test"), parameters, requestHeaders).toBlocking().first();
        assertThat(result.getStatus(), is(200));
        assertThat(new String(result.getData()), is(RESULT));
    }

    @Test
    public void postでリクエストができる() throws Exception {
        final ListMultimap<String, Object> parameters = ArrayListMultimap.create();
        parameters.put("hoge", "fuga");
        parameters.put("wawa", "abibi");
        final ListMultimap<String, Object> requestHeaders = ArrayListMultimap.create();
        requestHeaders.put("testHeader", "ababa");
        final RequestPattern pattern = new RequestPattern("/test", parameters, requestHeaders);
        final Response response = new Response(200, RESULT, null, CONTENT_TYPE);
        putResponse(pattern, response);
        final DefaultResponse result = client.post(getUrl("/test"), parameters, requestHeaders).toBlocking().first();
        assertThat(result.getStatus(), is(200));
        assertThat(new String(result.getData()), is(RESULT));
    }

    @Test
    public void putでリクエストができる() throws Exception {
        final ListMultimap<String, Object> parameters = ArrayListMultimap.create();
        parameters.put("hoge", "fuga");
        parameters.put("wawa", "abibi");
        final ListMultimap<String, Object> requestHeaders = ArrayListMultimap.create();
        requestHeaders.put("testHeader", "ababa");
        final RequestPattern pattern = new RequestPattern("/test", parameters, requestHeaders);
        final Response response = new Response(200, RESULT, null, CONTENT_TYPE);
        putResponse(pattern, response);
        final DefaultResponse result = client.put(getUrl("/test"), parameters, requestHeaders).toBlocking().first();
        assertThat(result.getStatus(), is(200));
        assertThat(new String(result.getData()), is(RESULT));
    }

    @Test
    public void deleteでリクエストができる() throws Exception {
        final ListMultimap<String, Object> parameters = ArrayListMultimap.create();
        parameters.put("hoge", "fuga");
        parameters.put("wawa", "abibi");
        final ListMultimap<String, Object> requestHeaders = ArrayListMultimap.create();
        requestHeaders.put("testHeader", "ababa");
        final RequestPattern pattern = new RequestPattern("/test", parameters, requestHeaders);
        final Response response = new Response(200, RESULT, null, CONTENT_TYPE);
        putResponse(pattern, response);
        final DefaultResponse result = client.delete(getUrl("/test"), parameters, requestHeaders).toBlocking().first();
        assertThat(result.getStatus(), is(200));
        assertThat(new String(result.getData()), is(RESULT));
    }
}
