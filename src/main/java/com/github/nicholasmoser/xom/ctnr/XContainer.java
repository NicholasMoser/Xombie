package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.*;
import com.github.nicholasmoser.xom.complex.Graph;
import com.github.nicholasmoser.xom.complex.XTexFont;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A container of values. The values can be other containers, tuples, or primitives.
 */
public class XContainer implements Value {
    private final String name;
    private final List<Value> values;
    private final XContainer parentClass;

    private XContainer(String name, List<Value> values) {
        this.name = name;
        this.values = values;
        this.parentClass = null;
    }

    private XContainer(String name, List<Value> values, XContainer parentClass) {
        this.name = name;
        this.values = values;
        this.parentClass = parentClass;
    }

    public static XContainer read(ByteStream bs, XomType type, List<XomType> xomTypes, StringTable stringTable) throws IOException {
        Map<String, XContainerDef> nameMap = XomScheme.getContainerNameMap();
        String typeName = type.name();
        XContainerDef container = nameMap.get(typeName);
        if (container == null) {
            throw new IOException("Failed to find definition for name " + typeName);
        }
        List<Value> values = new ArrayList<>();

        // All containers have 3 or 5 bytes following CTNR
        // TODO: Sometimes null but sometimes isn't. What are these bytes?
        if (!container.isNoCntr()) {
            bs.skipNBytes(3);
        }

        // Containers with special handling
        // TODO: I feel like these should be handled in XOMSCHM.xml but it would break passivity with xom2xml :/
        XType xType = XType.get(container.getName());
        if (xType != null) {
            switch(xType) {
                case XIntResourceDetails:
                    values.add(XInt.read("Value", bs));
                    values.add(XString.read("Name", bs, stringTable));
                    values.add(XUInt.read("Flags", bs));
                    return new XContainer(typeName, values);
                case XUintResourceDetails:
                    values.add(XUInt.read("Value", bs));
                    values.add(XString.read("Name", bs, stringTable));
                    values.add(XUInt.read("Flags", bs));
                    return new XContainer(typeName, values);
                case XStringResourceDetails:
                    values.add(XString.read("Value", bs, stringTable));
                    values.add(XString.read("Name", bs, stringTable));
                    values.add(XUInt.read("Flags", bs));
                    return new XContainer(typeName, values);
                case XFloatResourceDetails:
                    values.add(XFloat.read("Value", bs));
                    values.add(XString.read("Name", bs, stringTable));
                    values.add(XUInt.read("Flags", bs));
                    return new XContainer(typeName, values);
                case XContainerResourceDetails:
                    values.add(XUInt8.read("ContainerIndex", bs));
                    values.add(XString.read("Name", bs, stringTable));
                    values.add(XUInt.read("Flag", bs));
                    return new XContainer(typeName, values);
                case XVectorResourceDetails:
                    List<Value> tupleValues = new ArrayList<>();
                    tupleValues.add(XFloat.read("x", bs));
                    tupleValues.add(XFloat.read("y", bs));
                    tupleValues.add(XFloat.read("z", bs));
                    values.add(new Tuple("Value", tupleValues));
                    values.add(XString.read("Name", bs, stringTable));
                    values.add(XUInt.read("Flags", bs));
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
                case XBitmapDescriptor:
                    values.add(XString.read("ResourceId", bs, stringTable));
                    values.add(XUInt8.read("SectionId", bs));
                    values.add(XUInt8.read("SpriteScene", bs));
                    values.add(XUInt16.read("ImageWidth", bs));
                    values.add(XUInt16.read("ImageHeight", bs));
                    return new XContainer("XBitmapDescriptor", values);
                case XGraphSet:
                    int size = bs.readVarint();
                    for (int i = 0; i < size; i++) {
                        values.add(Graph.read("Graph", bs, stringTable));
                    }
                    return new XContainer("XGraphSet", values);
                case XTexFont:
                    values.add(XTexFont.read(bs, stringTable));
                    return new XContainer("XTexFont", values);
                default:
                    throw new IOException("TODO: Implement " + xType);
            }
        }
        // Normal containers, check children and parent class children
        List<XContainerDef> allChildren = new ArrayList<>();
        allChildren.addAll(container.getChildren());
        allChildren.addAll(XomScheme.getParentClassChildren(container.getParentClass()));
        for (XContainerDef child : allChildren) {
            String value = child.getValue();
            if (value == null) {
                String xTypeText = child.getXtype();
                if ("XCollection".equals(xTypeText)) {
                    // XCollection, basically an array of values
                    int size = bs.readByte();
                    values.add(XCollection.read(bs, child, size, stringTable));
                } else if (!child.getValueAttrs().isEmpty()) {
                    // Tuple with value attributes, such as x, y, and z
                    List<Value> tupleValues = new ArrayList<>();
                    for (Map.Entry<String, ValueType> entry : child.getValueAttrs().entrySet()) {
                        tupleValues.add(readValue(entry.getValue(), entry.getKey(), stringTable, bs));
                    }
                    values.add(new Tuple(child.getName(), tupleValues));
                } else {
                    // Anything else
                    System.out.println();
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
            case XInt -> XInt.read(name, bs);
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
