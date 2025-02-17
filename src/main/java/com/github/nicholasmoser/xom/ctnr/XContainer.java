package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A container of values. The values can be other containers, tuples, or primitives.
 */
public class XContainer {
    private final String name;
    private final List<Value> values;

    private XContainer(String name, List<Value> values) {
        this.name = name;
        this.values = values;
    }

    public static XContainer read(ByteStream bs, XomType type, StringTable stringTable) throws IOException {
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
        XType xType = XType.get(container.name());
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
                case XAlphaTest:
                case XBlendModeGL:
                case XCullFace:
                case XDataBank:
                case XDepthTest:
                case XGraphSet:
                case XImage:
                case XLightingEnable:
                case XOglTextureMap:
                case XTexFont:
                case XZBufferWriteEnable:
                    // Whitelist XContainers that can be handled automatically
                    break;
                default:
                    throw new IOException("TODO: Implement " + xType);
            }
        }
        // Grab children and parent class children to get list of all children
        List<XContainerDef> allChildren = new ArrayList<>();
        for (XContainerDef def : container.children()) {
            if (!"XRef".equals(def.id())) {
                // Don't add XReferences, just add their Values
                allChildren.add(def);
            }
        }
        allChildren.addAll(XomScheme.getParentClassChildren(container.parentClass()));
        for (XContainerDef child : allChildren) {
            String value = child.value();
            String xTypeText = child.xType();
            boolean isXCollection = "XCollection".equals(xTypeText);
            boolean hasParentXClass = child.parentClass() != null;
            boolean isXClass = "XClass".equals(xTypeText);
            boolean isXRef = "XRef".equals(child.id());
            if (isXCollection) {
                // XCollection, basically an array of values
                int size = bs.readVarint();
                values.add(XCollection.read(bs, child, container.name(), size, stringTable));
            } else if (value != null) {
                // Primitive type
                ValueType valueType = ValueType.get(value);
                if (valueType == null) {
                    throw new IOException("Primitve type now found: " + value);
                }
                values.add(ValueType.readValue(valueType, child.name(), container.name(), stringTable, bs));
            } else if (child.valueAttrs().size() == 1 && ValueType.getHref(child) != null) {
                // This is a single reference to another value by ID
                values.add(Ref.read(child.name(), bs));
            } else if (!child.valueAttrs().isEmpty()) {
                // Tuple with value attributes, such as x, y, and z
                List<Value> tupleValues = new ArrayList<>();
                for (Map.Entry<String, ValueType> entry : child.valueAttrs().entrySet()) {
                    tupleValues.add(ValueType.readValue(entry.getValue(), entry.getKey(), container.name(), stringTable, bs));
                }
                values.add(new Tuple(child.name(), tupleValues));
            } else {
                throw new IOException("Unknown type: " + child);
            }
        }
        return new XContainer(typeName, values);
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
