package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;

import java.io.IOException;

public class XInt implements Value {
    private final String name;
    private final int value;

    private XInt(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static XInt read(String name, ByteStream bs) throws IOException {
        return new XInt(name, ByteUtils.toInt32LE(bs.readNBytes(4)));
    }

    public int value() {
        return value;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public byte[] toBytes() {
        return ByteUtils.fromInt32LE(value);
    }

    @Override
    public String toString() {
        return "XInt{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
