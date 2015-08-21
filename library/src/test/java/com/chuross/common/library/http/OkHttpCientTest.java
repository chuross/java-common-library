package com.chuross.common.library.http;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OkHttpCientTest {

    private static final ListMultimap<String, Object> PARAMETERS = ArrayListMultimap.create();
    private static final ListMultimap<String, HeaderElement> REQUEST_HEADERS = ArrayListMultimap.create();
    private static final ListMultimap<String, Object> PATTERN_REQUEST_HEADERS = ArrayListMultimap.create();
    private OkHttpClient client;
    private MockWebServer server;
    private String url;

    static {
        PARAMETERS.put("hoge", "fuga");
        PARAMETERS.put("wawa", "abibi");
        REQUEST_HEADERS.put("testHeader", HeaderElement.of("ababa"));
        PATTERN_REQUEST_HEADERS.put("testHeader", "ababa");
    }

    @Before
    public void before() throws Exception {
        server = new MockWebServer();
        server.start(InetAddress.getByName("localhost"), 3000);
        server.enqueue(new MockResponse().setResponseCode(200));
        url = server.getUrl("/test").toString();
        client = new OkHttpClient();
    }

    @After
    public void after() throws Exception {
        server.shutdown();
    }

    @Test
    public void getでリクエストができる() throws Exception {
        client.get(url, PARAMETERS, REQUEST_HEADERS).toBlocking().single();
        final RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod(), is("GET"));
        assertThat(request.getPath(), is("/test?hoge=fuga&wawa=abibi"));
        assertThat(request.getHeaders().get("testHeader"), is("ababa"));
    }

    @Test
    public void postでリクエストができる() throws Exception {
        client.post(url, PARAMETERS, REQUEST_HEADERS).toBlocking().single();
        final RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod(), is("POST"));
        assertThat(request.getPath(), is("/test"));
        assertThat(request.getBody().readUtf8(), is("hoge=fuga&wawa=abibi"));
        assertThat(request.getHeaders().get("testHeader"), is("ababa"));
    }

    @Test
    public void putでリクエストができる() throws Exception {
        client.put(url, PARAMETERS, REQUEST_HEADERS).toBlocking().single();
        final RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod(), is("PUT"));
        assertThat(request.getPath(), is("/test"));
        assertThat(request.getBody().readUtf8(), is("hoge=fuga&wawa=abibi"));
        assertThat(request.getHeaders().get("testHeader"), is("ababa"));
    }

    @Test
    public void deleteでリクエストができる() throws Exception {
        client.delete(url, PARAMETERS, REQUEST_HEADERS).toBlocking().single();
        final RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod(), is("DELETE"));
        assertThat(request.getPath(), is("/test?hoge=fuga&wawa=abibi"));
        assertThat(request.getHeaders().get("testHeader"), is("ababa"));
    }
}
