package com.github.nicholasmoser.graphics;

import com.github.nicholasmoser.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Gfx {
    /**
     * Decodes compressed 8bit indexed (CI8) bytes.
     *
     * @param src The input bytes.
     * @param palette The palette.
     * @param width The width of the image.
     * @param height The height of the image.
     */
    public static int[] decodeCI8Image(byte[] src, Palette palette, int width, int height) {
        int[] dst = new int[src.length];
        int srcIndex = 0;
        int[] pal = bytesToInts(palette.data());
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
        return dst;
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

    /**
     * Converts CI8 encoded image index bytes to TGA encoded image bytes. TGA encoded image bytes specify
     * (width) x (height) color map indices.
     *
     * @param data The CI8 encoded bytes.
     * @param height The height of the image.
     * @param width The width of the image.
     * @return The TGA encoded bytes.
     */
    public static byte[] convertCI8IndicesToTGA(byte[] data, int height, int width) {
        byte[] buffer = new byte[data.length];
        int srcIndex = 0;
        for (int y = 0; y < height; y += 4) {
            for (int x = 0; x < width; x += 8) {
                for (int iy = 0; iy < 4; iy++, srcIndex += 8) {
                    int tdstIndex = (y + iy) * width + x;
                    System.arraycopy(data, srcIndex, buffer, tdstIndex, 8);
                }
            }
        }
        return buffer;
    }

    public static byte[] convertCI8ColorSpaceToTGA(byte[] data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
        try {
            for (int i = 0; i < data.length; i += 4) {
                byte[] bytes = new byte[] {data[i], data[i + 1], data[i + 2], data[i + 3]};
                baos.write(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return baos.toByteArray();
    }
}
