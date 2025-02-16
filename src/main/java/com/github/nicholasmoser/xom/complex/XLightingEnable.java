package com.github.nicholasmoser.xom.complex;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.ctnr.*;

import java.io.IOException;
import java.util.Map;

public class XLightingEnable implements XContainer {
    private final XBool enable;
    private final XBool twoSided;
    private final XBool localViewer;
    private final XEnum normalize;
    private final XVector4f ambientColor;

    private XLightingEnable(XBool enable, XBool twoSided, XBool localViewer, XEnum normalize, XVector4f ambientColor) {
        this.enable = enable;
        this.twoSided = twoSided;
        this.localViewer = localViewer;
        this.normalize = normalize;
        this.ambientColor = ambientColor;
    }

    private static final Map<Long, String> ENUMS = Map.of(0L, "kNormalizeNever",
            1L, "kNormalizeAlways",
            2L, "kNormalizeIfRequired");

    public static XLightingEnable read(ByteStream bs) throws IOException {
        XBool enable = XBool.read("Enable", bs);
        XBool twoSided = XBool.read("TwoSided", bs);
        XBool localViewer = XBool.read("LocalViewer", bs);
        XEnum normalize = XEnum.read("Normalize", bs, ENUMS);
        XVector4f ambientColor = XVector4f.read("AmbientColor", bs);
        return new XLightingEnable(enable, twoSided, localViewer, normalize, ambientColor);
    }

    @Override
    public String toString() {
        return "XLightingEnable{" +
                "enable=" + enable +
                ", twoSided=" + twoSided +
                ", localViewer=" + localViewer +
                ", normalize=" + normalize +
                ", ambientColor=" + ambientColor +
                '}';
    }
}
