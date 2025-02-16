package com.github.nicholasmoser.xom.complex;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.ctnr.*;

import java.io.IOException;
import java.util.Map;

public class XAlphaTest implements XContainer {
    private final XBool enable;
    private final XEnum compareFunction;
    private final XFloat refValue;

    private XAlphaTest(XBool enable, XEnum compareFunction, XFloat refValue) {
        this.enable = enable;
        this.compareFunction = compareFunction;
        this.refValue = refValue;
    }

    private static final Map<Long, String> ENUMS = Map.of(0L, "kCompareFunctionNever",
            1L, "kCompareFunctionLess",
            2L, "kCompareFunctionEqual",
            3L, "kCompareFunctionLessEqual",
            4L, "kCompareFunctionGreater",
            5L, "kCompareFunctionNotEqual",
            6L, "kCompareFunctionGreaterEqual",
            7L, "kCompareFunctionAlways");

    public static XAlphaTest read(ByteStream bs) throws IOException {
        XBool enabled = XBool.read("Enable", bs);
        XEnum compareFunction = XEnum.read("CompareFunction", bs, ENUMS);
        XFloat refValue = XFloat.read("RefValue", bs);
        return new XAlphaTest(enabled, compareFunction, refValue);
    }

    @Override
    public String toString() {
        return "XAlphaTest{" +
                "enable=" + enable +
                ", compareFunction=" + compareFunction +
                ", refValue=" + refValue +
                '}';
    }
}
