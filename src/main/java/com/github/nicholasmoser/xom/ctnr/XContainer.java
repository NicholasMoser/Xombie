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
                case XBitmapDescriptor:
                case XAlphaTest:
                case XBlendModeGL:
                case XContainerResourceDetails:
                case XCullFace:
                case XCustomDescriptor:
                case XDataBank:
                case XDepthTest:
                case XFloatResourceDetails:
                case XGraphSet:
                case XImage:
                case XIntResourceDetails:
                case XLightingEnable:
                case XOglTextureMap:
                case XStringResourceDetails:
                case XTexFont:
                case XUintResourceDetails:
                case XVectorResourceDetails:
                case XZBufferWriteEnable:
                    // Whitelist XContainers that can be handled automatically
                    break;
                default:
                    throw new IOException("TODO: Implement " + xType);
            }
        }
        List<XContainerDef> allChildren = getAllChildren(container);
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

    /**
     * Get the list of all children of this container definition, as well as the children of its parent class.
     * The order of the children is important here, if a parent has an element before the subclass, that element
     * should come before the subclass elements. So the order is basically:
     *
     * <ol>
     *     <li>Parent class children BEFORE this subclass</li>
     *     <li>The children of this subclass</li>
     *     <li>Parent class children AFTER this subclass</li>
     * </ol>
     * @param container The container definition to get all children for.
     * @return All children of the container definition.
     */
    private static List<XContainerDef> getAllChildren(XContainerDef container) throws IOException {
        List<XContainerDef> allChildren = new ArrayList<>();
        allChildren.addAll(XomScheme.getParentClassChildrenBefore(container.name(), container.parentClass()));
        for (XContainerDef def : container.children()) {
            if (!"XRef".equals(def.id())) {
                // Don't add XReferences, just add their Values
                allChildren.add(def);
            }
        }
        allChildren.addAll(XomScheme.getParentClassChildrenAfter(container.name(), container.parentClass()));
        return allChildren;
    }

    public String name() {
        return name;
    }

    public List<Value> values() {
        return values;
    }

    public byte[] toBytes() {
        return new byte[1];
    }

    @Override
    public String toString() {
        return "XContainer{" +
                "name='" + name + '\'' +
                ", values=" + values +
                '}';
    }
}
