package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;

import java.io.IOException;
import java.util.Map;

public class XEnum implements Value {
    private final String name;
    private final long value;
    private final String mappedValue;

    private XEnum(String name, long value, String mappedValue) {
        this.name = name;
        this.value = value;
        this.mappedValue = mappedValue;
    }

    public static XEnum read(String name, ByteStream bs, Map<Long, String> mapping) throws IOException {
        long value = ByteUtils.toUint32LE(bs.readNBytes(4));
        String mappedValue = mapping.get(value);
        return new XEnum(name, value, mappedValue);
    }

    @Override
    public String toString() {
        return "XEnum{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", mappedValue='" + mappedValue + '\'' +
                '}';
    }
}
