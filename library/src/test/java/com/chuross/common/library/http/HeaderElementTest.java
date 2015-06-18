package com.chuross.common.library.http;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HeaderElementTest {

    @Test
    public void パラメータを表現できる() throws Exception {
        assertThat(HeaderElement.of("hoge=fuga").toString(), is("hoge=fuga"));
        assertThat(HeaderElement.of("hoge").toString(), is("hoge"));
        assertThat(HeaderElement.of("hoge = fuga").toString(), is("hoge=fuga"));
        assertThat(HeaderElement.of("hoge=fuga;").toString(), is("hoge=fuga"));
    }

    @Test
    public void パラメータを結合できる() {
        HeaderElement element1 = new HeaderElement("key1", "value1");
        HeaderElement element2 = new HeaderElement("key2", "value2");
        HeaderElement element3 = new HeaderElement("key3", "value3");
        assertThat(HeaderElement.mergeToString(element1, element2, element3), is("key1=value1;key2=value2;key3=value3"));
    }
}
