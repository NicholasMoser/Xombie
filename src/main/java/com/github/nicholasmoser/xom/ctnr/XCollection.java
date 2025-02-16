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

    public static Value read(ByteStream bs, XContainerDef child, int size, StringTable stringTable) throws IOException {
        List<Value> values = new ArrayList<>();
        // Populate collection
        for (int i = 0; i < size; i++) {
            if (!child.getValueAttrs().isEmpty()) {
                // Collection of tuples (e.g. x, y, z)
                List<Value> tupleValues = new ArrayList<>(3);
                for (Map.Entry<String, ValueType> entry : child.getValueAttrs().entrySet()) {
                    tupleValues.add(XContainerGeneric.readValue(entry.getValue(), entry.getKey(), stringTable, bs));
                }
                values.add(new Tuple(child.getName(), tupleValues));
            } else if (child.getValue() != null ) {
                // Collection of value (e.g. XInt)
                throw new IOException("TODO");

            } else if (child.getHref().equals("XContainer")) {
                // Collection of XContainers
                throw new IOException("TODO");
            }
        }
        return new XCollection(child.getName(), values);
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
}
