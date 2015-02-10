package com.chuross.common.library.api;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

public class IntegerBooleanConverter implements Converter<Boolean> {

    @Override
    public Boolean read(final InputNode node) throws Exception {
        return node.getValue().equals("1");
    }

    @Override
    public void write(final OutputNode node, final Boolean value) throws Exception {
        node.setValue(value ? "1" : "0");
    }

}
