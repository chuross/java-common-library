package com.chuross.common.library.http.rest;

import com.chuross.common.library.http.*;
import com.google.common.collect.ListMultimap;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func1;

import java.net.InetAddress;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RestClientTest {

    private MockWebServer server;
    private TestRestClient client;

    @Before
    public void before() throws Exception {
        server = new MockWebServer();
        server.start(InetAddress.getByName("localhost"), 3000);
        client = new TestRestClient(new DefaultHttpClient());
    }

    @After
    public void after() throws Exception {
        server.shutdown();
    }

    @Test
    public void リクエストができる() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("hogeBody"));
        final Result<String> result = client.executeTest(new RestRequestBuilder(server.getUrl("/test").toString()).addParameter("hoge", "fuga").addParameter("wawa", "abibi").addRequestHeader("testHeader", "ababa").build()).toBlocking().single();
        assertThat(result.getStatus(), is(200));
        assertThat(result.getContent(), is("hogeBody"));
        final RecordedRequest request = server.takeRequest();
        assertThat(request.getMethod(), is("GET"));
        assertThat(request.getPath(), is("/test?hoge=fuga&wawa=abibi"));
        assertThat(request.getHeader("testHeader"), is("ababa"));
    }

    private class TestRestClient extends RestClient {

        public TestRestClient(final HttpClient<DefaultResponse> client) {
            super(client);
        }

        public Observable<Result<String>> executeTest(final RestRequest request) {
            return execute(Method.GET, request, new Func1<com.chuross.common.library.http.Response, Result<String>>() {
                @Override
                public Result<String> call(final com.chuross.common.library.http.Response response) {
                    return new Result<String>() {
                        @Override
                        public int getStatus() {
                            return response.getStatus();
                        }

                        @Override
                        public boolean isSuccess() {
                            return response.getStatus() == 200;
                        }

                        @Override
                        public ListMultimap<String, HeaderElement> getHeaders() {
                            return null;
                        }

                        @Override
                        public String getContent() {
                            return new String(response.getData());
                        }
                    };
                }
            });
        }
    }
}
