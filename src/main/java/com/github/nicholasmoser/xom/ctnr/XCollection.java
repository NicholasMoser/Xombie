package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.StringTable;
import com.github.nicholasmoser.xom.ValueType;
import com.github.nicholasmoser.xom.XContainerDef;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XCollection implements Value {
    private final String name;
    private final List<Value> values;

    private XCollection(String name, List<Value> values) {
        this.name = name;
        this.values = values;
    }

    public static Value read(ByteStream bs, XContainerDef child, StringTable stringTable) throws IOException {
        List<Value> values = new ArrayList<>();
        for (Map.Entry<String, ValueType> entry : child.getValueAttrs().entrySet()) {
            values.add(XContainer.readValue(entry.getValue(), entry.getKey(), stringTable, bs));
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
