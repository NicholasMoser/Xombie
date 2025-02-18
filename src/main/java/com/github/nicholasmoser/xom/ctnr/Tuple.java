package com.github.nicholasmoser.xom.ctnr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * An ordered collection of primitive values, such as coordinates like x, y, and z.
 */
public class Tuple implements Value {
    private final String name;
    private final List<Value> values;

    public Tuple(String name, List<Value> values) {
        this.name = name;
        this.values = values;
    }

    public String name() {
        return name;
    }

    public List<Value> values() {
        return values;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "name='" + name + '\'' +
                ", values=" + values +
                '}';
    }

    @Override
    public byte[] toBytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (Value value : values) {
                baos.write(value.toBytes());
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
