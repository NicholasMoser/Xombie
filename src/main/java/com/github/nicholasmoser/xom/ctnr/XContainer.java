package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XContainer implements Value {
    private static final String XIntResourceDetails = "XIntResourceDetails";
    private static final String XUintResourceDetails = "XUintResourceDetails";
    private static final String XContainerResourceDetails = "XContainerResourceDetails";
    private static final String XDataBank = "XDataBank";
    private static final String XCustomDescriptor = "XCustomDescriptor";
    private final String name;
    private final List<Value> values;

    private XContainer(String name, List<Value> values) {
        this.name = name;
        this.values = values;
    }

    public static XContainer read(ByteStream bs, XomType type, List<XomType> xomTypes, StringTable stringTable) throws IOException {
        Map<String, XContainerDef> nameMap = XomScheme.getContainerNameMap();
        String typeName = type.name();
        XContainerDef container = nameMap.get(typeName);
        if (container == null) {
            throw new IOException("Failed to find definition for name " + typeName);
        }
        List<Value> values = new ArrayList<>();

        // Containers with special handling
        // TODO: I feel like these should be handled in XOMSCHM.xml but it would break passivity with xom2xml :/
        switch(container.getName()) {
            case XIntResourceDetails:
            case XUintResourceDetails:
                values.add(XUInt.read("Value", bs));
                values.add(XString.read("Name", bs, stringTable));
                values.add(XUInt.read("Flag", bs));
                return new XContainer(typeName, values);
            case XContainerResourceDetails:
                values.add(XUInt8.read("ContainerIndex", bs));
                values.add(XString.read("Name", bs, stringTable));
                values.add(XUInt.read("Flag", bs));
                return new XContainer(typeName, values);
            case XDataBank:
                values.add(XUInt8.read("Section", bs));
                List<XContainerDef> children = container.getChildren();
                for (int i = 1; i < children.size(); i++) {
                    XContainerDef child = children.get(i);
                    String href = child.getHref();
                    XomType xomType = getXomType(xomTypes, href);
                    XUInt8 pointer = XUInt8.read(child.getName(), bs);
                    values.add(pointer);
                    if (pointer.value() != 0 && xomType != null) {
                        for (int j = 0; j < xomType.size(); j++) {
                            values.add(XUInt8.read("ContainerIndex", bs));
                        }
                    }
                }
                return new XContainer(typeName, values);
            case XCustomDescriptor:
                values.add(XString.read("XBaseResourceDescriptor", bs, stringTable));
                return new XContainer(typeName, values);
            default:
                break;
        }
        // Normal containers
        for (XContainerDef child : container.getChildren()) {
            String value = child.getValue();
            if (value == null) {
                String xType = child.getXtype();
                if (!"XCollection".equals(xType)) {
                    throw new IOException("Unknown value and xType, xType is " + xType);
                }
                int size = bs.readByte();
                for (int i = 0; i < size; i++) {
                    values.add(XCollection.read(bs, child, stringTable));
                }
            } else {
                ValueType valueType = ValueType.valueOf(value);
                values.add(readValue(valueType, child.getName(), stringTable, bs));
            }
        }
        return new XContainer(typeName, values);
    }

    public static Value readValue(ValueType valueType, String name, StringTable stringTable, ByteStream bs) throws IOException {
        return switch (valueType) {
            case XFloat -> XFloat.read(name, bs);
            case XBool -> XBool.read(name, bs);
            case XString -> XString.read(name, bs, stringTable);
            case XUInt -> XUInt.read(name, bs);
            case XUInt8 -> XUInt8.read(name, bs);
            case XUInt16 -> XUInt16.read(name, bs);
            default -> throw new IOException("Type not yet implemented: " + valueType);
        };
    }

    private static XomType getXomType(List<XomType> xomTypes, String name) {
        for (XomType xomType : xomTypes) {
            if (name.equals(xomType.name())) {
                return xomType;
            }
        }
        return null;
    }

    public String name() {
        return name;
    }

    public List<Value> values() {
        return values;
    }

    @Override
    public String toString() {
        return "XContainer{" +
                "name='" + name + '\'' +
                ", values=" + values +
                '}';
    }
}
