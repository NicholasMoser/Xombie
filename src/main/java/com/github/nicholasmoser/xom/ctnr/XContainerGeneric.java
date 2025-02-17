package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.*;
import com.github.nicholasmoser.xom.complex.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A container of values. The values can be other containers, tuples, or primitives.
 */
public class XContainerGeneric implements XContainer {
    private final String name;
    private final List<Value> values;
    private final XContainerGeneric parentClass;

    private XContainerGeneric(String name, List<Value> values) {
        this.name = name;
        this.values = values;
        this.parentClass = null;
    }

    private XContainerGeneric(String name, List<Value> values, XContainerGeneric parentClass) {
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
                    return new XContainerGeneric(typeName, values);
                case XUintResourceDetails:
                    values.add(XUInt.read("Value", bs));
                    values.add(XString.read("Name", bs, stringTable));
                    values.add(XUInt.read("Flags", bs));
                    return new XContainerGeneric(typeName, values);
                case XStringResourceDetails:
                    values.add(XString.read("Value", bs, stringTable));
                    values.add(XString.read("Name", bs, stringTable));
                    values.add(XUInt.read("Flags", bs));
                    return new XContainerGeneric(typeName, values);
                case XFloatResourceDetails:
                    values.add(XFloat.read("Value", bs));
                    values.add(XString.read("Name", bs, stringTable));
                    values.add(XUInt.read("Flags", bs));
                    return new XContainerGeneric(typeName, values);
                case XContainerResourceDetails:
                    values.add(XUInt8.read("ContainerIndex", bs));
                    values.add(XString.read("Name", bs, stringTable));
                    values.add(XUInt.read("Flag", bs));
                    return new XContainerGeneric(typeName, values);
                case XVectorResourceDetails:
                    List<Value> tupleValues = new ArrayList<>();
                    tupleValues.add(XFloat.read("x", bs));
                    tupleValues.add(XFloat.read("y", bs));
                    tupleValues.add(XFloat.read("z", bs));
                    values.add(new Tuple("Value", tupleValues));
                    values.add(XString.read("Name", bs, stringTable));
                    values.add(XUInt.read("Flags", bs));
                    return new XContainerGeneric(typeName, values);
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
                    return new XContainerGeneric(typeName, values);
                case XCustomDescriptor:
                    values.add(XString.read("XBaseResourceDescriptor", bs, stringTable));
                    return new XContainerGeneric(typeName, values);
                case XBitmapDescriptor:
                    values.add(XString.read("ResourceId", bs, stringTable));
                    values.add(XUInt8.read("SectionId", bs));
                    values.add(XUInt8.read("SpriteScene", bs));
                    values.add(XUInt16.read("ImageWidth", bs));
                    values.add(XUInt16.read("ImageHeight", bs));
                    return new XContainerGeneric("XBitmapDescriptor", values);
                case XGraphSet:
                    int size = bs.readVarint();
                    for (int i = 0; i < size; i++) {
                        values.add(Graph.read("Graph", bs, stringTable));
                    }
                    return new XContainerGeneric("XGraphSet", values);
                case XTexFont:
                case XAlphaTest:
                case XZBufferWriteEnable:
                case XDepthTest:
                case XCullFace:
                case XLightingEnable:
                case XBlendModeGL:
                case XImage:
                    // Whitelist XContainers that can be handled automatically
                    break;
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
            String xTypeText = child.getXtype();
            boolean isXCollection = "XCollection".equals(xTypeText);
            boolean isXContainer = "XContainer".equals(child.getHref());
            boolean hasParentXClass = child.getParentClass() != null;
            boolean isXClass = "XClass".equals(xTypeText);
            boolean isXRef = "XRef".equals(child.getId());
            if (isXCollection) {
                // XCollection, basically an array of values
                int size = bs.readVarint();
                values.add(XCollection.read(bs, child, container.getName(), size, stringTable));
            } else if (value != null) {
                // Primitive type
                ValueType valueType = ValueType.valueOf(value);
                values.add(ValueType.readValue(valueType, child.getName(), container.getName(), stringTable, bs));
            } else if (!child.getValueAttrs().isEmpty()) {
                // Tuple with value attributes, such as x, y, and z
                List<Value> tupleValues = new ArrayList<>();
                for (Map.Entry<String, ValueType> entry : child.getValueAttrs().entrySet()) {
                    tupleValues.add(ValueType.readValue(entry.getValue(), entry.getKey(), container.getName(), stringTable, bs));
                }
                values.add(new Tuple(child.getName(), tupleValues));
            } else if (hasParentXClass) {
                // We can skip if this is a subclass.
            } else if (isXContainer) {
                values.add(Ref.read(child.getName(), bs));
            } else {
                throw new IOException("Unknown type: " + child);
            }
        }
        return new XContainerGeneric(typeName, values);
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
