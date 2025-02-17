package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.StringTable;

import java.io.IOException;

public class XString implements Value {
    private final String name;
    private final String value;

    private XString(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static XString read(String name, ByteStream bs, StringTable stringTable) throws IOException {
        int str_index = bs.readVarint();
        if (str_index == -1) {
            throw new IOException("Tried to read XString but at end of stream");
        }
        String string_val = stringTable.getString(str_index);
        if (string_val == null) {
            throw new IOException("Missing string from string table at index " + str_index);
        }
        return new XString(name, string_val);
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return "XString{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public byte[] toBytes() {
        throw new RuntimeException("TODO");
    }
}
