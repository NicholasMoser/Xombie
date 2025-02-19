package com.github.nicholasmoser.graphics;

import com.github.nicholasmoser.xom.ctnr.*;

public class TGAReader {
    public static TGA fromXContainer(XContainer container, Palette palette) {
        XString name = (XString) container.values().get(0);
        XUInt16 width = (XUInt16) container.values().get(1);
        XUInt16 height = (XUInt16) container.values().get(2);
        XEnum format = (XEnum) container.values().get(7);
        XCollection data = (XCollection) container.values().get(8);
        byte[] bytes = XCollection.fromByteXCollection(data);

        return new TGA.Builder()
                .dataTypeCode((byte) 0x2)
                .width((short) width.value())
                .height((short) height.value())
                .bitsPerPixel((byte) 0x20)
                .data(bytes)
                .fileName(name.value())
                .format(format.mappedValue())
                .palette(palette)
                .build();
    }
}
