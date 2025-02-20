package com.github.nicholasmoser.graphics;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.ctnr.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.github.nicholasmoser.xom.ctnr.XEnumMaps.IMAGE_FORMATS;

public class TGAUtil {

    public static void replaceTGA(XContainer container, TGA tga) {
        List<Value> values = container.values();
        values.remove(1);
        values.add(1, new XUInt16("Width", tga.width()));
        values.remove(2);
        values.add(2, new XUInt16("Height", tga.height()));
        values.remove(7);
        values.add(7, new XEnum("Format", IMAGE_FORMATS.inverse().get(tga.format()), tga.format()));
        values.remove(8);
        values.add(8, bytesToDataCollection(tga.data()));
        values.remove(9);
        values.add(9, new Ref("Palette", 0));
    }

    private static XCollection bytesToDataCollection(byte[] bytes) {
        List<Value> values = new ArrayList<>(bytes.length);
        for (byte datum : bytes) {
            values.add(new XByte(datum));
        }
        return new XCollection("Data", values);
    }

    /**
     * Read a TGA object from an XImage XContainer
     *
     * @param container The XImage XContainer
     * @param palette The palette to use or null if no palette
     * @return The TGA object.
     */
    public static TGA readFromXContainer(XContainer container, Palette palette) {
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

    /**
     * Read a TGA object from a TGA file path.
     *
     * @param filePath The TGA file path.
     * @return The TGA object.
     * @throws IOException If any I/O exception occurs.
     */
    public static TGA readFromFile(Path filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(filePath);
        ByteStream bs = new ByteStream(bytes);
        TGA.Builder tga = new TGA.Builder();
        tga.idLength(bs.readOneByte());
        tga.colourMapType(bs.readOneByte());
        byte dataTypeCode = bs.readOneByte();
        tga.dataTypeCode(dataTypeCode);
        tga.colourMapOrigin(bs.readShortLE());
        tga.colourMapLength(bs.readShortLE());
        tga.colourMapDepth(bs.readOneByte());
        tga.xOrigin(bs.readShortLE());
        tga.yOrigin(bs.readShortLE());
        short width = bs.readShortLE();
        tga.width(width);
        short height = bs.readShortLE();
        tga.height(height);
        byte bitsPerPixel = bs.readOneByte();
        tga.bitsPerPixel(bitsPerPixel);
        tga.imageDescriptor(bs.readOneByte());
        int bytesToRead = (width * height * bitsPerPixel) / 8;
        tga.data(bs.readNBytes(bytesToRead));
        tga.format("kImageFormat_A8R8G8B8");
        if (bitsPerPixel != (byte) 32 && dataTypeCode != 2) {
            throw new IOException(String.format("TODO: bitsPerPixel %d dataTypeCode %d", bitsPerPixel, dataTypeCode));
        }
        return tga.build();
    }
}
