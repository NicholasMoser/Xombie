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
        // TODO: Add ability to modify palettes
        //values.remove(9);
        //values.add(9, new Ref("Palette", 0));
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
                .idLength((byte) 0)
                .colourMapType((byte) (palette != null ? 1 : 0))
                .dataTypeCode((byte) (palette != null ? 1 : 2))
                .colourMapOrigin((short) 0)
                .colourMapLength((short) (palette != null ? palette.data().length / 4 : 0))
                .colourMapDepth((byte) (palette != null ? 0x20 : 0))
                .xOrigin((short) 0)
                .yOrigin((short) 0)
                .width((short) width.value())
                .height((short) height.value())
                .bitsPerPixel((byte) (palette != null ? 0x8 : 0x10))
                // Bit 5 (0x20 or 0010 0000 in binary) is set
                // This indicates that the image is stored top-left origin instead of the default bottom-left origin.
                .imageDescriptor((byte) 0x20)
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
        short colorMapLength = bs.readShortLE();
        tga.colourMapLength(colorMapLength);
        byte colorMapDepth = bs.readOneByte();
        tga.colourMapDepth(colorMapDepth);
        tga.xOrigin(bs.readShortLE());
        tga.yOrigin(bs.readShortLE());
        short width = bs.readShortLE();
        tga.width(width);
        short height = bs.readShortLE();
        tga.height(height);
        byte bitsPerPixel = bs.readOneByte();
        tga.bitsPerPixel(bitsPerPixel);
        // Bit 5 (0x20 or 0010 0000 in binary) is set
        // This indicates that the image is stored top-left origin instead of the default bottom-left origin.
        byte imageDescriptor = bs.readOneByte();
        boolean flip = false;
        if (imageDescriptor != 0x20) {
            // We need to flip the image. GameCube images use top-left origin but this is bottom-left origin.
            flip = true;
        }
        tga.imageDescriptor(imageDescriptor);
        if (dataTypeCode == 2) {
            tga.format("kImageFormat_A8R8G8B8");
            int bytesToRead = (width * height * bitsPerPixel) / 8;
            tga.data(bs.readNBytes(bytesToRead));
        } else if (dataTypeCode == 1) {
            tga.format("kImageFormat_NgcCI8");
            tga.palette(readPalette(bs, colorMapDepth, colorMapLength));
            int bytesToRead = (width * height * bitsPerPixel) / 8;
            byte[] buffer = bs.readNBytes(bytesToRead);
            tga.data(Gfx.convertTGAIndicesToCI8(buffer, height, width, flip));
        } else {
            throw new IOException("TODO: " + dataTypeCode);
        }
        return tga.build();
    }

    private static Palette readPalette(ByteStream bs, byte colorMapDepth, short colorMapLength) throws IOException {
        if (colorMapDepth <= 0) {
            throw new IllegalArgumentException("Invalid colorMapDepth: " + colorMapDepth);
        } else if (colorMapLength <= 0) {
            throw new IllegalArgumentException("Invalid colorMapLength: " + colorMapLength);
        }
        int colorByteSize = colorMapDepth / 8;
        byte[] colorMap = bs.readNBytes(colorByteSize * colorMapLength);
        return new Palette(8, 0, "kPaletteFormat_R8G8B8A8", colorMap);
    }
}
