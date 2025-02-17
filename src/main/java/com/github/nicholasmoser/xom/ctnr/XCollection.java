package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.StringTable;
import com.github.nicholasmoser.xom.ValueType;
import com.github.nicholasmoser.xom.XContainerDef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An ordered collection of values. Can be a collection of primitive values, tuples, or XContainers.
 */
public class XCollection implements Value {
    private final String name;
    private final List<Value> values;

    private XCollection(String name, List<Value> values) {
        this.name = name;
        this.values = values;
    }

    public static XCollection read(ByteStream bs, XContainerDef child, String parentName, int size, StringTable stringTable) throws IOException {
        List<Value> values = new ArrayList<>();
        // Populate collection
        String value = child.value();
        for (int i = 0; i < size; i++) {
            if (child.valueAttrs().size() == 1 && ValueType.getHref(child) != null) {
                // This is a single reference to another value by ID
                values.add(Ref.read(child.name(), bs));
            } else if (!child.valueAttrs().isEmpty()) {
                // Collection of tuples (e.g. x, y, z)
                List<Value> tupleValues = new ArrayList<>(3);
                for (Map.Entry<String, ValueType> entry : child.valueAttrs().entrySet()) {
                    tupleValues.add(ValueType.readValue(entry.getValue(), entry.getKey(), parentName, stringTable, bs));
                }
                values.add(new Tuple(child.name(), tupleValues));
            } else if (value != null ) {
                // Collection of value (e.g. XInt)
                ValueType valueType = ValueType.valueOf(value);
                if (valueType == ValueType.XBase64Byte) {
                    byte[] bytes = bs.readNBytes(size);
                    List<Value> data = new ArrayList<>(bytes.length);
                    for (byte datum : bytes) {
                        data.add(new XByte(datum));
                    }
                    return new XCollection(child.name(), data);
                }
                values.add(ValueType.readValue(valueType, child.name(), parentName, stringTable, bs));
            }
        }
        return new XCollection(child.name(), values);
    }

    private static ValueType getType(Map<String, ValueType> valueAttrs) {
        for (Map.Entry<String, ValueType> entry : valueAttrs.entrySet()) {
            if ("Type".equals(entry.getKey())) {
                return entry.getValue();
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
        return "XCollection{" +
                "name='" + name + '\'' +
                ", values=" + values +
                '}';
    }

    @Override
    public byte[] toBytes() {
        throw new RuntimeException("TODO");
    }
}
