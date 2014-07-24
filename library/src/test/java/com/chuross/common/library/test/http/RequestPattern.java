package com.chuross.common.library.test.http;

import com.chuross.common.library.util.HttpClientUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.http.Header;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RequestPattern {

    private String path;
    private String parameter;
    private List<Header> requestHeaders;

    public RequestPattern(String path) {
        this(path, "", new ArrayList<Header>());
    }

    public RequestPattern(String path, List<NameValuePair> parameters, List<Header> requestHeaders) {
        this(path, getParameterString(parameters), requestHeaders);
    }

    public RequestPattern(String path, String parameter, List<Header> requestHeaders) {
        this.path = path;
        this.parameter = parameter;
        this.requestHeaders = requestHeaders;

        if(this.requestHeaders != null && this.requestHeaders.size() > 0) {
            sortRequestHeaders();
        }
    }

    private static String getParameterString(List<NameValuePair> parameters) {
        if(parameters == null || parameters.size() <= 0) {
            return "";
        }

        Collections.sort(parameters, new Comparator<NameValuePair>() {
            @Override
            public int compare(NameValuePair o1, NameValuePair o2) {
                int compare = o1.getName().compareTo(o2.getName());
                if (compare != 0) {
                    return compare;
                }
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        return HttpClientUtils.getParameterString(parameters);
    }

    private void sortRequestHeaders() {
        Collections.sort(this.requestHeaders, new Comparator<Header>() {
            @Override
            public int compare(Header o1, Header o2) {
                int compare = o1.getName().compareTo(o2.getName());
                if(compare != 0) {
                    return compare;
                }
                return o1.getValue().compareTo(o2.getValue());
            }
        });
    }

    public String getPath() {
        return path;
    }

    public String getParameter() {
        return parameter;
    }

    public List<Header> getRequestHeaders() {
        return requestHeaders;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  RequestPattern)) {
            return false;
        }
        RequestPattern pattern = (RequestPattern) obj;

        EqualsBuilder builder = new EqualsBuilder();
        builder.append(path, pattern.getPath());
        builder.append(parameter, pattern.getParameter());

        List<Header> targetRequestHeaders = pattern.getRequestHeaders();
        builder.append(requestHeaders.size(), targetRequestHeaders.size());
        for(int i = 0; i < requestHeaders.size(); i++) {
            builder.append(requestHeaders.get(i).getName(), targetRequestHeaders.get(i).getName());
            builder.append(requestHeaders.get(i).getValue(), targetRequestHeaders.get(i).getValue());
        }
        return builder.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(path);
        builder.append(parameter);
        for (Header header : requestHeaders) {
            builder.append(header.getName());
            builder.append(header.getValue());
        }
        return builder.build();
    }
}