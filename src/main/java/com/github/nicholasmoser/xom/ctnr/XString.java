package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.ByteUtils;
import com.github.nicholasmoser.xom.StringTable;

import java.io.IOException;

public class XString implements Value {
    private final String name;
    private final String value;
    private final int strIndex;

    private XString(String name, String value, int strIndex) {
        this.name = name;
        this.value = value;
        this.strIndex = strIndex;
    }

    public static XString read(String name, ByteStream bs, StringTable stringTable) throws IOException {
        int strIndex = bs.readVarint();
        if (strIndex == -1) {
            throw new IOException("Tried to read XString but at end of stream");
        }
        String stringVal = stringTable.getString(strIndex);
        if (stringVal == null) {
            throw new IOException("Missing string from string table at index " + strIndex);
        }
        return new XString(name, stringVal, strIndex);
    }

    public String value() {
        return value;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public byte[] toBytes() {
        return ByteUtils.writeVarint(strIndex);
    }

    @Override
    public String toString() {
        return "XString{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", strIndex=" + strIndex +
                '}';
    }
}
