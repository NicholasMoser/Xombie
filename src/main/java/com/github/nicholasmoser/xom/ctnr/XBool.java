package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;

import java.io.IOException;

public class XBool implements Value {
    private final String name;
    private final boolean value;

    private XBool(String name, boolean value) {
        this.name = name;
        this.value = value;
    }

    public static XBool read(String name, ByteStream bs) throws IOException {
        int boolNum = bs.read();
        if (boolNum == -1) {
            throw new IOException("Tried to read XBool but at end of stream");
        }
        return new XBool(name, boolNum == 1);
    }

    public boolean value() {
        return value;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public byte[] toBytes() {
        if (value) {
            return new byte[] { 0x1 };
        }
        return new byte[] { 0x0 };
    }

    @Override
    public String toString() {
        return "XBool{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
