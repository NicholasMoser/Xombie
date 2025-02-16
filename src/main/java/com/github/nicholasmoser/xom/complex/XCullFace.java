package com.github.nicholasmoser.xom.complex;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.ctnr.*;

import java.io.IOException;
import java.util.Map;

public class XCullFace implements XContainer {
    private final XEnum cullMode;

    private XCullFace(XEnum cullMode) {
        this.cullMode = cullMode;
    }

    private static final Map<Long, String> ENUMS = Map.of(0L, "kCullModeOff",
            1L, "kCullModeFront",
            2L, "kCullModeBack",
            3L, "kCullModeForceFront",
            4L, "kCullModeForceBack");

    public static XCullFace read(ByteStream bs) throws IOException {
        XEnum cullMode = XEnum.read("CullMode", bs, ENUMS);
        return new XCullFace(cullMode);
    }

    @Override
    public String toString() {
        return "XCullFace{" +
                "cullMode=" + cullMode +
                '}';
    }
}
