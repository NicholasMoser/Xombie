package com.github.nicholasmoser.graphics;

import com.github.nicholasmoser.xom.ctnr.*;

import java.util.List;

public class Palette {
    private final int indexSize;
    private final int flags;
    private final String format;
    private final byte[] data;

    /**
     * A palette used for indexed color graphics.
     *
     * @param indexSize The index size.
     * @param flags The flags.
     * @param format The palette format.
     * @param data The palette data.
     */
    public Palette(int indexSize, int flags, String format, byte[] data) {
        this.indexSize = indexSize;
        this.flags = flags;
        this.format = format;
        this.data = data;
    }

    public String format() {
        return format;
    }

    public byte[] data() {
        return data;
    }

    @Override
    public String toString() {
        return "Palette{" +
                "indexSize=" + indexSize +
                ", flags=" + flags +
                ", format='" + format + '\'' +
                ", dataLength=" + data.length +
                '}';
    }

    public static Palette get(XContainer imageContainer, List<XContainer> allContainers) {
        Ref ref = (Ref) imageContainer.values().get(9);
        if (ref.value() == 0) {
            return null;
        }
        XContainer palette = allContainers.get(ref.value() - 1);
        XUInt8 indexSize = (XUInt8) palette.values().get(0);
        XUInt8 flags = (XUInt8) palette.values().get(1);
        XEnum format = (XEnum) palette.values().get(2);
        XCollection data = (XCollection) palette.values().get(3);
        byte[] bytes = XCollection.fromXUint8Collection(data);
        return new Palette(indexSize.value(), flags.value(), format.mappedValue(), bytes);
    }
}
