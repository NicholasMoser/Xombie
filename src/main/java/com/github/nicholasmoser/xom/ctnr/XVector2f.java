package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;

import java.io.IOException;

public class XVector2f implements Value {
    private final String name;
    private final XFloat x;
    private final XFloat y;

    private XVector2f(String name, XFloat x, XFloat y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
    public static XVector2f read(String name, ByteStream bs) throws IOException {
        XFloat x = XFloat.read("x", bs);
        XFloat y = XFloat.read("y", bs);
        return new XVector2f(name, x, y);
    }

    @Override
    public String toString() {
        return "XVector2f{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
