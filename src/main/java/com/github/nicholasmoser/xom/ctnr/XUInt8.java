package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;

import java.io.IOException;

public class XUInt8 implements Value {
    private final int value;

    private XUInt8(int value) {
        this.value = value;
    }

    public static XUInt8 read(ByteStream bs) throws IOException {
        return new XUInt8(bs.readByte());
    }

    public int get() {
        return value;
    }
}
