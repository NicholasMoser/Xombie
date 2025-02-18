package com.github.nicholasmoser.xom.ctnr;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.GUID;

import java.io.IOException;

public class XGUID implements Value {
    private String name;
    private String xGUID;

    private XGUID(String name, String xGUID) {
        this.name = name;
        this.xGUID = xGUID;
    }

    public static XGUID read(String name, ByteStream bs) throws IOException {
        byte[] bytes = bs.readNBytes(0x10);
        return new XGUID(name, GUID.bytesToString(bytes));
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public byte[] toBytes() {
        return GUID.stringToBytes(xGUID);
    }

    @Override
    public String toString() {
        return "XGUID{" +
                "name='" + name + '\'' +
                ", xGUID='" + xGUID + '\'' +
                '}';
    }
}
