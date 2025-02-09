package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;

import java.io.IOException;

public class XBool implements Value {
    private final boolean value;

    private XBool(boolean value) {
        this.value = value;
    }

    public static XBool read(ByteStream bs) throws IOException {
        int bool_num = bs.read();
        if (bool_num == -1) {
            throw new IOException("Tried to read XBool but at end of stream");
        }
        return new XBool(bool_num == 1);
    }
}
