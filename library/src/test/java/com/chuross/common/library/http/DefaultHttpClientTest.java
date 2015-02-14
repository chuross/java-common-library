package com.chuross.common.library.http;

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
    private static final ListMultimap<String, Object> PARAMETERS = ArrayListMultimap.create();
    private static final ListMultimap<String, HeaderElement> REQUEST_HEADERS = ArrayListMultimap.create();
    private static final ListMultimap<String, Object> PATTERN_REQUEST_HEADERS = ArrayListMultimap.create();
    private DefaultHttpClient client;

    static {
        PARAMETERS.put("hoge", "fuga");
        PARAMETERS.put("wawa", "abibi");
        REQUEST_HEADERS.put("testHeader", HeaderElement.of("ababa"));
        PATTERN_REQUEST_HEADERS.put("testHeader", "ababa");
    }

    @Before
    public void before() {
        client = new DefaultHttpClient();
        final RequestPattern pattern = new RequestPattern("/test", PARAMETERS, PATTERN_REQUEST_HEADERS);
        final Response response = new Response(200, RESULT, null, MediaType.JSON_UTF_8);
        putResponse(pattern, response);
    }

    @Test
    public void getでリクエストができる() throws Exception {
        final DefaultResponse result = client.get(getUrl("/test"), PARAMETERS, REQUEST_HEADERS).toBlocking().single();
        assertThat(result.getStatus(), is(200));
        assertThat(new String(result.getData()), is(RESULT));
    }

    @Test
    public void postでリクエストができる() throws Exception {
        final DefaultResponse result = client.post(getUrl("/test"), PARAMETERS, REQUEST_HEADERS).toBlocking().single();
        assertThat(result.getStatus(), is(200));
        assertThat(new String(result.getData()), is(RESULT));
    }

    @Test
    public void putでリクエストができる() throws Exception {
        final DefaultResponse result = client.put(getUrl("/test"), PARAMETERS, REQUEST_HEADERS).toBlocking().single();
        assertThat(result.getStatus(), is(200));
        assertThat(new String(result.getData()), is(RESULT));
    }

    @Test
    public void deleteでリクエストができる() throws Exception {
        final DefaultResponse result = client.delete(getUrl("/test"), PARAMETERS, REQUEST_HEADERS).toBlocking().single();
        assertThat(result.getStatus(), is(200));
        assertThat(new String(result.getData()), is(RESULT));
    }
}
