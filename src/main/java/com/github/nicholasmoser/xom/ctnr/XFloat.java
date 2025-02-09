package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;

import java.io.IOException;

public class XFloat implements Value {
    private final String name;
    private final float value;

    private XFloat(String name, float value) {
        this.name = name;
        this.value = value;
    }

    public static XFloat read(String name, ByteStream bs) throws IOException {
        return new XFloat(name, bs.readLEFloat());
    }

    @Override
    public String toString() {
        return "XFloat{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
