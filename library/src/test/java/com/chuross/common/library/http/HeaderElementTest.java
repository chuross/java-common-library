package com.chuross.common.library.http;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HeaderElementTest {

    @Test
    public void パラメータを表現できる() {
        assertThat(HeaderElement.of("hoge=fuga").toString(), is("hoge=fuga"));
        assertThat(HeaderElement.of("hoge").toString(), is("hoge"));
        assertThat(HeaderElement.of("hoge = fuga").toString(), is("hoge=fuga"));
        assertThat(HeaderElement.of("hoge=fuga;").toString(), is("hoge=fuga"));
    }
}
