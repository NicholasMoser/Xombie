package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.ctnr.*;

import java.io.IOException;
import java.util.Map;

/**
 * A primitive value type for xom files.
 */
public enum ValueType {
    XInt,
    XUInt,
    XColor4ub,
    XUIntHex,
    XBool,
    XString,
    XFloat,
    XVector2f,
    XVector3f,
    XVector4f,
    XMatrix3,
    XMatrix34,
    XMatrix,
    XBoundBox,
    XKey,
    XEnum,
    XInt8,
    XUInt8,
    XInt16,
    XUInt16,
    XBitmap16,
    XColor4444,
    XGUID,
    XContainer,
    XBase64Byte;

    public static ValueType get(String name) {
        try {
            return ValueType.valueOf(name);
        } catch (Exception e) {
            return null;
        }
    }

    public static Value readValue(ValueType valueType, String name, String parentName, StringTable stringTable, ByteStream bs) throws IOException {
        return switch (valueType) {
            case XFloat -> com.github.nicholasmoser.xom.ctnr.XFloat.read(name, bs);
            case XBool -> com.github.nicholasmoser.xom.ctnr.XBool.read(name, bs);
            case XString -> com.github.nicholasmoser.xom.ctnr.XString.read(name, bs, stringTable);
            case XInt -> com.github.nicholasmoser.xom.ctnr.XInt.read(name, bs);
            case XUInt -> com.github.nicholasmoser.xom.ctnr.XUInt.read(name, bs);
            case XUInt8 -> com.github.nicholasmoser.xom.ctnr.XUInt8.read(name, bs);
            case XUInt16 -> com.github.nicholasmoser.xom.ctnr.XUInt16.read(name, bs);
            case XEnum -> com.github.nicholasmoser.xom.ctnr.XEnum.read(name, parentName, bs);
            case XVector2f -> com.github.nicholasmoser.xom.ctnr.XVector2f.read(name, bs);
            case XVector4f -> com.github.nicholasmoser.xom.ctnr.XVector4f.read(name, bs);
            case XGUID -> com.github.nicholasmoser.xom.ctnr.XGUID.read(name, bs);
            case XContainer -> com.github.nicholasmoser.xom.ctnr.Ref.read(name, bs);
            default -> throw new IOException("Type not yet implemented: " + valueType);
        };
    }

    public static ValueType getHref(XContainerDef container) {
        for (Map.Entry<String, ValueType> entry : container.getValueAttrs().entrySet()) {
            if ("href".equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
