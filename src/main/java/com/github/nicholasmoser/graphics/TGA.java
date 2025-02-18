package com.github.nicholasmoser.graphics;

import java.util.Arrays;

public class TGA {
    // https://www.gc-forever.com/yagcd/chap17.html
    private byte idLength; //  The idlength is the length of a string located after the header.
    private byte colourMapType;
    /*
        Data Type field values:
        0  -  No image data included.
        1  -  Uncompressed, color-mapped images.
        2  -  Uncompressed, RGB images.
        3  -  Uncompressed, black and white images.
        9  -  Runlength encoded color-mapped images.
       10  -  Runlength encoded RGB images.
       11  -  Compressed, black and white images.
       32  -  Compressed color-mapped data, using Huffman, Delta, and
              runlength encoding.
       33  -  Compressed color-mapped data, using Huffman, Delta, and
              runlength encoding.  4-pass quadtree-type process.
     */
    private final byte dataTypeCode;
    private final short colourMapOrigin;
    private final short colourMapLength;
    private final byte colourMapDepth;
    private final short xOrigin;
    private final short yOrigin;
    private final short width;
    private final short height;
    private final byte bitsPerPixel; // The bitsperpixel specifies the size of each colour value.
    private final byte imageDescriptor;
    private final byte[] data;
    private final String fileName;

    TGA(byte idLength, byte colourMapType, byte dataTypeCode, short colourMapOrigin, short colourMapLength, byte colourMapDepth, short xOrigin, short yOrigin, short width, short height, byte bitsPerPixel, byte imageDescriptor, byte[] data, String fileName) {
        this.idLength = idLength;
        this.colourMapType = colourMapType;
        this.dataTypeCode = dataTypeCode;
        this.colourMapOrigin = colourMapOrigin;
        this.colourMapLength = colourMapLength;
        this.colourMapDepth = colourMapDepth;
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        this.width = width;
        this.height = height;
        this.bitsPerPixel = bitsPerPixel;
        this.imageDescriptor = imageDescriptor;
        this.data = data;
        this.fileName = fileName;
    }

    public static class Builder {
        private byte idLength;
        private byte colourMapType;
        private byte dataTypeCode;
        private short colourMapOrigin;
        private short colourMapLength;
        private byte colourMapDepth;
        private short xOrigin;
        private short yOrigin;
        private short width;
        private short height;
        private byte bitsPerPixel;
        private byte imageDescriptor;
        private byte[] data;
        private String fileName;

        public Builder idLength(byte idLength) {
            this.idLength = idLength;
            return this;
        }

        public Builder colourMapType(byte colourMapType) {
            this.colourMapType = colourMapType;
            return this;
        }

        public Builder dataTypeCode(byte dataTypeCode) {
            this.dataTypeCode = dataTypeCode;
            return this;
        }

        public Builder colourMapOrigin(short colourMapOrigin) {
            this.colourMapOrigin = colourMapOrigin;
            return this;
        }

        public Builder colourMapLength(short colourMapLength) {
            this.colourMapLength = colourMapLength;
            return this;
        }

        public Builder colourMapDepth(byte colourMapDepth) {
            this.colourMapDepth = colourMapDepth;
            return this;
        }

        public Builder xOrigin(short xOrigin) {
            this.xOrigin = xOrigin;
            return this;
        }

        public Builder yOrigin(short yOrigin) {
            this.yOrigin = yOrigin;
            return this;
        }

        public Builder width(short width) {
            this.width = width;
            return this;
        }

        public Builder height(short height) {
            this.height = height;
            return this;
        }

        public Builder bitsPerPixel(byte bitsPerPixel) {
            this.bitsPerPixel = bitsPerPixel;
            return this;
        }

        public Builder imageDescriptor(byte imageDescriptor) {
            this.imageDescriptor = imageDescriptor;
            return this;
        }

        public Builder data(byte[] data) {
            this.data = data;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public TGA build() {
            return new TGA(idLength, colourMapType, dataTypeCode, colourMapOrigin, colourMapLength, colourMapDepth, xOrigin, yOrigin, width, height, bitsPerPixel, imageDescriptor, data, fileName);
        }
    }


}
