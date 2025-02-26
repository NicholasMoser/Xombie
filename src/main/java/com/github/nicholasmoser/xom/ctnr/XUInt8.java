package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;

import java.io.IOException;

public class XUInt8 implements Value {
    private final String name;
    private final int value;

    public XUInt8(int value) {
        this.name = null;
        this.value = value;
    }

    public XUInt8(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static XUInt8 read(String name, ByteStream bs) throws IOException {
        return new XUInt8(name, bs.readByte());
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
        return new byte[] {(byte) value};
    }

    @Override
    public String toString() {
        return "XUInt8{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
