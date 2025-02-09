package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;

import java.io.IOException;

public class XUInt implements Value {
    private final long value;

    private XUInt(long value) {
        this.value = value;
    }

    public static XUInt read(ByteStream bs) throws IOException {
        return new XUInt(ByteUtils.toUint32LE(bs.readNBytes(4)));
    }
}
