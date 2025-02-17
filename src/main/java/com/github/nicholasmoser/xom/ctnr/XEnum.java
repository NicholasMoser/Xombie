package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;

import java.io.IOException;
import java.util.Map;

public class XEnum implements Value {
    private final String name;
    private final long value;
    private final String mappedValue;

    private XEnum(String name, long value, String mappedValue) {
        this.name = name;
        this.value = value;
        this.mappedValue = mappedValue;
    }

    public static XEnum read(String name, String parentName, ByteStream bs) throws IOException {
        long value = ByteUtils.toUint32LE(bs.readNBytes(4));
        String key = parentName + "/" + name;
        String mappedValue = switch (key) {
            case "XAlphaTest/CompareFunction", "XDepthTest/CompareFunction" -> XEnumMaps.COMPARE_FUNCTIONS.get(value);
            case "XCullFace/CullMode" -> XEnumMaps.CULL_MODES.get(value);
            case "XLightingEnable/Normalize" -> XEnumMaps.NORMALIZE.get(value);
            case "XBlendModeGL/SourceFactor", "XBlendModeGL/DestFactor" -> XEnumMaps.FACTORS.get(value);
            case "XImage/Format" -> XEnumMaps.IMAGE_FORMATS.get(value);
            default -> throw new IOException("TODO");
        };
        return new XEnum(name, value, mappedValue);
    }

    @Override
    public String toString() {
        return "XEnum{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", mappedValue='" + mappedValue + '\'' +
                '}';
    }
}
