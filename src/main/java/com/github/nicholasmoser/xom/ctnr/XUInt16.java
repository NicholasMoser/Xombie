package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;

import java.io.IOException;

public class XUInt16 implements Value {
    private final String name;
    private final int value;

    private XUInt16(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static XUInt16 read(String name, ByteStream bs) throws IOException {
        return new XUInt16(name, ByteUtils.toUint16LE(bs.readNBytes(2)));
    }
}
