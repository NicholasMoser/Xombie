package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;

import java.io.IOException;

public class XUInt implements Value {
    private final String name;
    private final long value;

    private XUInt(String name, long value) {
        this.name = name;
        this.value = value;
    }

    public static XUInt read(String name, ByteStream bs) throws IOException {
        return new XUInt(name, ByteUtils.toUint32LE(bs.readNBytes(4)));
    }

    public String name() {
        return name;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return "XUInt{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public byte[] toBytes() {
        throw new RuntimeException("TODO");
    }
}
