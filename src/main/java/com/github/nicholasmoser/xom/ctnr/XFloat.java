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

    public float value() {
        return value;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public byte[] toBytes() {
        throw new RuntimeException("TODO");
    }

    @Override
    public String toString() {
        return "XFloat{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
