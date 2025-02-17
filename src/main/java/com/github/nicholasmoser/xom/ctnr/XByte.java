package com.github.nicholasmoser.xom.ctnr;

public class XByte implements Value {
    private final byte data;

    public XByte(byte data) {
        this.data = data;
    }

    public byte value() {
        return data;
    }

    @Override
    public String toString() {
        return "XByte{" +
                "data=" + data +
                '}';
    }

    @Override
    public byte[] toBytes() {
        throw new RuntimeException("TODO");
    }
}
