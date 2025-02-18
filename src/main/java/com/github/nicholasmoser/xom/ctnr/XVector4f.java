package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class XVector4f implements Value {
    private final String name;
    private final XFloat x;
    private final XFloat y;
    private final XFloat z;
    private final XFloat w;

    private XVector4f(String name, XFloat x, XFloat y, XFloat z, XFloat w) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public static XVector4f read(String name, ByteStream bs) throws IOException {
        XFloat x = XFloat.read("x", bs);
        XFloat y = XFloat.read("y", bs);
        XFloat z = XFloat.read("z", bs);
        XFloat w = XFloat.read("w", bs);
        return new XVector4f(name, x, y, z, w);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public byte[] toBytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(16);
            baos.write(ByteUtils.fromFloatLE(x.value()));
            baos.write(ByteUtils.fromFloatLE(y.value()));
            baos.write(ByteUtils.fromFloatLE(z.value()));
            baos.write(ByteUtils.fromFloatLE(w.value()));
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "XVector4f{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", w=" + w +
                '}';
    }
}
