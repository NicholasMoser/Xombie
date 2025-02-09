package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;

import java.io.IOException;

public class XFloat implements Value {
    private final float value;

    private XFloat(float value) {
        this.value = value;
    }

    public static XFloat read(ByteStream bs) throws IOException {
        return new XFloat(bs.readLEFloat());
    }
}
