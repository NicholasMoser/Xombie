package com.github.nicholasmoser.graphics;

import java.nio.ByteBuffer;

public class Gfx {
    /**
     * Decodes compressed 8bit indexed (CI8) bytes.
     *
     * @param dst The output bytes.
     * @param src The input bytes.
     * @param palette The palette.
     * @param width The width of the image.
     * @param height The height of the image.
     */
    public static void decodeCI8Image(int[] dst, byte[] src, Palette palette, int width, int height) {
        int srcIndex = 0;
        int[] pal = bytesToInts(palette.data());
        String paletteFormat = palette.format();
        if (!"kPaletteFormat_R8G8B8A8".equals(paletteFormat)) {
            throw new IllegalArgumentException("TODO: " + paletteFormat);
        }

        for (int y = 0; y < height; y += 4) {
            for (int x = 0; x < width; x += 8) {
                for (int iy = 0; iy < 4; iy++, srcIndex += 8) {
                    int tdstIndex = (y + iy) * width + x;
                    for (int ix = 0; ix < 8; ix++) {
                        int value = pal[Byte.toUnsignedInt(src[srcIndex + ix])];
                        dst[tdstIndex + ix] = value;
                    }
                }
            }
        }
    }

    /**
     * Convert a byte array to an int array by combining 4 bytes into an int.
     *
     * @param byteArray The byte array to convert.
     * @return The converted int array.
     */
    public static int[] bytesToInts(byte[] byteArray) {
        if (byteArray.length % 4 != 0) {
            throw new IllegalArgumentException("Byte array length must be a multiple of 4");
        }

        int[] intArray = new int[byteArray.length / 4];
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);

        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = buffer.getInt();
        }

        return intArray;
    }
}
