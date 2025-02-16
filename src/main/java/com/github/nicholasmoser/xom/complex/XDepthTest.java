package com.github.nicholasmoser.xom.complex;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.ctnr.*;

import java.io.IOException;
import java.util.Map;

public class XDepthTest implements XContainer {
    private final XBool enable;
    private final XEnum compareFunction;
    private final XFloat nearZ;
    private final XFloat farZ;

    private XDepthTest(XBool enable, XEnum compareFunction, XFloat nearZ, XFloat farZ) {
        this.enable = enable;
        this.compareFunction = compareFunction;
        this.nearZ = nearZ;
        this.farZ = farZ;
    }

    private static final Map<Long, String> ENUMS = Map.of(0L, "kCompareFunctionNever",
            1L, "kCompareFunctionLess",
            2L, "kCompareFunctionEqual",
            3L, "kCompareFunctionLessEqual",
            4L, "kCompareFunctionGreater",
            5L, "kCompareFunctionNotEqual",
            6L, "kCompareFunctionGreaterEqual",
            7L, "kCompareFunctionAlways");

    public static XDepthTest read(ByteStream bs) throws IOException {
        XEnum compareFunction = XEnum.read("CompareFunction", bs, ENUMS);
        XBool enable = XBool.read("Enable", bs);
        XFloat nearZ = XFloat.read("NearZ", bs);
        XFloat farZ = XFloat.read("FarZ", bs);
        return new XDepthTest(enable, compareFunction, nearZ, farZ);
    }

    @Override
    public String toString() {
        return "XDepthTest{" +
                "enable=" + enable +
                ", compareFunction=" + compareFunction +
                ", nearZ=" + nearZ +
                ", farZ=" + farZ +
                '}';
    }
}
