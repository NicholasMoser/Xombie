package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;

import java.io.IOException;

/**
 * A reference to an XContainer. The reference value is the container ID.
 */
public class Ref implements Value {
    private final String name;
    private int value;

    public Ref(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static Ref read(String name, ByteStream bs) throws IOException {
        return new Ref(name, bs.readVarint());
    }

    public int value() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public byte[] toBytes() {
        return ByteUtils.writeVarint(value);
    }

    @Override
    public String toString() {
        return "Ref{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
