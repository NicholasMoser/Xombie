package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;

import java.io.IOException;

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
            case "XOglTextureMap/Blend" -> XEnumMaps.BLENDS.get(value);
            case "XOglTextureMap/AddressModeS", "XOglTextureMap/AddressModeT" -> XEnumMaps.ADDRESS_MODES.get(value);
            case "XOglTextureMap/MagFilter", "XOglTextureMap/MinFilter", "XOglTextureMap/MipFilter" -> XEnumMaps.FILTER_MODES.get(value);
            default -> throw new IOException("TODO: " + key);
        };
        return new XEnum(name, value, mappedValue);
    }

    public long value() {
        return value;
    }

    public String mappedValue() {
        return mappedValue;
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
        return "XEnum{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", mappedValue='" + mappedValue + '\'' +
                '}';
    }
}
