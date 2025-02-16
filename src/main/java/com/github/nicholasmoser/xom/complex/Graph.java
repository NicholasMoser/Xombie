package com.github.nicholasmoser.xom.complex;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.StringTable;
import com.github.nicholasmoser.xom.ctnr.Value;
import com.github.nicholasmoser.xom.ctnr.XGUID;
import com.github.nicholasmoser.xom.ctnr.XString;
import com.github.nicholasmoser.xom.ctnr.XUInt8;

import java.io.IOException;

public class Graph implements Value {
    private final XGUID type; // GUID
    private final XUInt8 href;
    private final XString name;

    private Graph(String containerName, XGUID type, XUInt8 href, XString name) {
        this.type = type;
        this.href = href;
        this.name = name;
    }

    public static Graph read(String name, ByteStream bs, StringTable stringTable) throws IOException {
        XGUID xGUID = XGUID.read("Type", bs);
        XUInt8 href = XUInt8.read("href", bs);
        XString innerName = XString.read("Name", bs, stringTable);
        return new Graph(name, xGUID, href, innerName);
    }

    @Override
    public String toString() {
        return "XGraphSet{" +
                "type=" + type +
                ", href=" + href +
                ", name=" + name +
                '}';
    }
}
