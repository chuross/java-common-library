package com.chuross.common.library.rest;

import com.chuross.common.library.http.DefaultHttpClient;
import com.chuross.common.library.http.DefaultResponse;
import com.chuross.common.library.http.HttpClient;
import com.chuross.testcase.http.HttpRequestTestCase;
import com.chuross.testcase.http.RequestPattern;
import com.chuross.testcase.http.Response;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.net.MediaType;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.functions.Func1;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RestClientTest extends HttpRequestTestCase {

    private static final String RESULT = "hoge";
    private static final ListMultimap<String, Object> PARAMETERS = ArrayListMultimap.create();
    private static final ListMultimap<String, Object> REQUEST_HEADERS = ArrayListMultimap.create();
    private TestRestClient client;

    static {
        PARAMETERS.put("hoge", "fuga");
        PARAMETERS.put("wawa", "abibi");
        REQUEST_HEADERS.put("testHeader", "ababa");
    }

    @Before
    public void before() {
        client = new TestRestClient(new DefaultHttpClient());
        final RequestPattern pattern = new RequestPattern("/test", PARAMETERS, REQUEST_HEADERS);
        final Response response = new Response(200, RESULT, null, MediaType.JSON_UTF_8);
        putResponse(pattern, response);
    }

    @Test
    public void リクエストができる() throws Exception {
        final Result<String> result = client.executeTest().toBlocking().single();
        assertThat(result.getStatus(), is(200));
        assertThat(result.getContent(), is("hoge"));
    }

    private class TestRestClient extends RestClient<DefaultResponse> {

        public TestRestClient(final HttpClient<DefaultResponse> client) {
            super(client);
        }

        public Observable<Result<String>> executeTest() {
            final RestRequest request = new RestRequestBuilder(BASE_URL + "/test").addParameter("hoge", "fuga").addParameter("wawa", "abibi").addRequestHeader("testHeader", "ababa").build();
            return execute(Method.GET, request, new Func1<DefaultResponse, Result<String>>() {
                @Override
                public Result<String> call(final DefaultResponse response) {
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
                        public ListMultimap<String, Object> getHeaders() {
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
