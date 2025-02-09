package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.github.nicholasmoser.xom.ctnr.*;

import java.io.IOException;
import java.util.ArrayList;
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
        List<Value> values = new ArrayList<>();

        // Containers with special handling
        switch(container.getName()) {
            // TODO: I feel like these should be handled in XOMSCHM.xml but it would break passivity with xom2xml :/
            case XIntResourceDetails:
            case XUintResourceDetails:
                XUInt value = XUInt.read(bs);
                XString intName = XString.read(bs, stringTable);
                XUInt flag = XUInt.read(bs);
                // TODO: Store
                return;
            case XContainerResourceDetails:
                XUInt8 childContainerIndex = XUInt8.read(bs);
                XString containerName = XString.read(bs, stringTable);
                XUInt containerFlag = XUInt.read(bs);
                return;
            case XDataBank:
                XUInt8 section = XUInt8.read(bs);
                List<XContainer> children = container.getChildren();
                for (int i = 1; i < children.size(); i++) {
                    XContainer child = children.get(i);
                    String href = child.getHref();
                    XomType xomType = getXomType(xomTypes, href);
                    XUInt8 pointer = XUInt8.read(bs);
                    if (pointer.get() != 0 && xomType != null) {
                        for (int j = 0; j < xomType.size(); j++) {
                            XUInt8 next = XUInt8.read(bs);
                        }
                    }
                }
                return;
            default:
                break;
        }
        // Normal containers
        for (XContainer child : container.getChildren()) {
            String value = child.getValue();
            if (value == null) {
                throw new IOException("Value is null, unable to find value type");
            }
            ValueType valueType = ValueType.valueOf(value);
            switch(valueType) {
                case XFloat:
                    values.add(XFloat.read(bs));
                    break;
                case XBool:
                    values.add(XBool.read(bs));
                    break;
                case XString:
                    values.add(XString.read(bs, stringTable));
                    break;
                case XUInt:
                    values.add(XUInt.read(bs));
                    break;
                case XUInt8:
                    values.add(XUInt8.read(bs));
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
}
