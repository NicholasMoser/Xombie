package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ContainerParser {
    private static final String XIntResourceDetails = "XIntResourceDetails";
    private static final String XUintResourceDetails = "XUintResourceDetails";
    private static final String XContainerResourceDetails = "XContainerResourceDetails";
    private static final String XDataBank = "XDataBank";

    public static void parse(ByteStream bs, XomType type, List<XomType> xomTypes, StringTable stringTable) throws IOException {
        Map<String, XContainer> nameMap = XomScheme.getContainerNameMap();
        String typeName = type.name();
        XContainer container = nameMap.get(typeName);
        if (container == null) {
            throw new IOException("Failed to find definition for name " + typeName);
        }
        switch(container.getName()) {
            // TODO: I feel like these should be handled in XOMSCHM.xml but it would break passivity with xom2xml :/
            case XIntResourceDetails:
            case XUintResourceDetails:
                long value = ByteUtils.toUint32LE(bs.readNBytes(4));
                String intName = readXString(bs, stringTable);
                long flag = ByteUtils.toUint32LE(bs.readNBytes(4));
                // TODO: Store
                return;
            case XContainerResourceDetails:
                int childContainerIndex = bs.readByte();
                String containerName = readXString(bs, stringTable);
                long containerFlag = ByteUtils.toUint32LE(bs.readNBytes(4));
                return;
            case XDataBank:
                int section = bs.readByte();
                List<XContainer> children = container.getChildren();
                for (int i = 1; i < children.size(); i++) {
                    XContainer child = children.get(i);
                    String href = child.getHref();
                    XomType xomType = getXomType(xomTypes, href);
                    int pointer = bs.readByte();
                    if (pointer != 0 && xomType != null) {
                        for (int j = 0; j < xomType.size(); j++) {
                            int next = bs.readByte();
                        }
                    }
                }
                return;
            default:
                break;
        }
        for (XContainer child : container.getChildren()) {
            String value = child.getValue();
            if (value == null) {
                throw new IOException("Value is null, unable to find value type");
            }
            ValueType valueType = ValueType.valueOf(value);
            switch(valueType) {
                case XFloat:
                    float float_val = bs.readLEFloat();
                    // TODO: Store
                    break;
                case XBool:
                    boolean bool_val = readXBool(bs);
                    // TODO: Store
                    break;
                case XString:
                    String string_val = readXString(bs, stringTable);
                    // TODO: Store
                    break;
                case XUInt:
                    long uint_val = ByteUtils.toUint32LE(bs.readNBytes(4));
                    // TODO: Store
                    break;
                case XUInt8:
                    int uint8_val = bs.readByte();
                    // TODO: Store
                    break;
                default:
                    throw new IOException("Type not yet implemented: " + valueType);

            }
        }
        System.out.println();
    }

    private static XomType getXomType(List<XomType> xomTypes, String name) {
        for (XomType xomType : xomTypes) {
            if (name.equals(xomType.name())) {
                return xomType;
            }
        }
        return null;
    }

    private static boolean readXBool(ByteStream bs) throws IOException {
        int bool_num = bs.read();
        if (bool_num == -1) {
            throw new IOException("Tried to read XBool but at end of stream");
        }
        return bool_num == 1;
    }

    private static String readXString(ByteStream bs, StringTable stringTable) throws IOException {
        int str_index = bs.read();
        if (str_index == -1) {
            throw new IOException("Tried to read XString but at end of stream");
        }
        String string_val = stringTable.getString(str_index);
        if (string_val == null) {
            throw new IOException("Missing string from string table at index " + str_index);
        }
        return string_val;
    }
}
