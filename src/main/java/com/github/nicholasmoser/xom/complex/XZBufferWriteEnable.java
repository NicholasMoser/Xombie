package com.github.nicholasmoser.xom.complex;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.ctnr.XBool;
import com.github.nicholasmoser.xom.ctnr.XContainer;

import java.io.IOException;

public class XZBufferWriteEnable implements XContainer {
    private final XBool enable;

    private XZBufferWriteEnable(XBool enable) {
        this.enable = enable;
    }

    public static XZBufferWriteEnable read(ByteStream bs) throws IOException {
        XBool enable = XBool.read("Enable", bs);
        return new XZBufferWriteEnable(enable);
    }

    @Override
    public String toString() {
        return "XZBufferWriteEnable{" +
                "enable=" + enable +
                '}';
    }
}
