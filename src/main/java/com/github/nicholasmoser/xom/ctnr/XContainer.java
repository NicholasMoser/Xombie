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
                throw new IOException("Value is null, unable to find value type");
            }
            ValueType valueType = ValueType.valueOf(value);
            switch(valueType) {
                case XFloat:
                    values.add(XFloat.read(child.getName(), bs));
                    break;
                case XBool:
                    values.add(XBool.read(child.getName(), bs));
                    break;
                case XString:
                    values.add(XString.read(child.getName(), bs, stringTable));
                    break;
                case XUInt:
                    values.add(XUInt.read(child.getName(), bs));
                    break;
                case XUInt8:
                    values.add(XUInt8.read(child.getName(), bs));
                    break;
                case XUInt16:
                    values.add(XUInt16.read(child.getName(), bs));
                    break;
                default:
                    throw new IOException("Type not yet implemented: " + valueType);

            }
        }
        return new XContainer(typeName, values);
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
