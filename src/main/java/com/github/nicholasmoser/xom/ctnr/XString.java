package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.StringTable;

import java.io.IOException;

public class XString implements Value {
    private final String value;

    private XString(String value) {
        this.value = value;
    }

    public static XString read(ByteStream bs, StringTable stringTable) throws IOException {
        int str_index = bs.read();
        if (str_index == -1) {
            throw new IOException("Tried to read XString but at end of stream");
        }
        String string_val = stringTable.getString(str_index);
        if (string_val == null) {
            throw new IOException("Missing string from string table at index " + str_index);
        }
        return new XString(string_val);
    }
}
