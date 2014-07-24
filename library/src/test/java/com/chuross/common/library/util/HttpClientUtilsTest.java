package com.chuross.common.library.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.chuross.common.library.http.HttpResponse;
import com.chuross.common.library.test.HttpRequestTestCase;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.List;

public class HttpClientUtilsTest extends HttpRequestTestCase {

    private static final String RESULT = "{\"hoge\": \"fuga\"}";
    private static final String ENCODING = "UTF-8";
    private static final String CONTENT_TYPE = "application/json";

    @Test
    public void getでリクエストができる() throws Exception {
        List<NameValuePair> parameters = Lists.newArrayList();
        parameters.add(new BasicNameValuePair("hoge", "fuga"));
        parameters.add(new BasicNameValuePair("wawa", "abibi"));

        List<Header> requestHeaders = Lists.newArrayList();
        requestHeaders.add(new BasicHeader("testHeader", "ababa"));

        RequestPattern pattern = new RequestPattern("/gettest", parameters, requestHeaders);
        Response response = new Response(200, RESULT, ENCODING, CONTENT_TYPE, null);
        addResponse(pattern, response);

        HttpResponse result1 = HttpClientUtils.get(MoreExecutors.sameThreadExecutor(), getUrl("/gettest"), null, null, null, 1).get();
        assertThat(result1.getStatus(), is(404));

        HttpResponse result2 = HttpClientUtils.get(MoreExecutors.sameThreadExecutor(), getUrl("/gettest"), parameters, requestHeaders, null, 1).get();
        assertThat(result2.getStatus(), is(200));
        assertThat(result2.getContentsAsString(ENCODING), is(RESULT));
    }

}
