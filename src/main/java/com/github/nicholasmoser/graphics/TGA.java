package com.github.nicholasmoser.graphics;

import java.util.Arrays;

public class TGA {
    private byte idlength; //  The idlength is the length of a string located after the header.
    private byte colourmaptype;
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
    private final byte datatypecode;
    private final short colourmaporigin;
    private final short colourmaplength;
    private final byte colourmapdepth;
    private final short x_origin;
    private final short y_origin;
    private final short width;
    private final short height;
    private final byte bitsperpixel; // The bitsperpixel specifies the size of each colour value.
    private final byte imagedescriptor;
    private final byte[] data;

    private TGA(byte idlength, byte colourmaptype, byte datatypecode, short colourmaporigin, short colourmaplength, byte colourmapdepth, short x_origin, short y_origin, short width, short height, byte bitsperpixel, byte imagedescriptor, byte[] data) {
        this.idlength = idlength;
        this.colourmaptype = colourmaptype;
        this.datatypecode = datatypecode;
        this.colourmaporigin = colourmaporigin;
        this.colourmaplength = colourmaplength;
        this.colourmapdepth = colourmapdepth;
        this.x_origin = x_origin;
        this.y_origin = y_origin;
        this.width = width;
        this.height = height;
        this.bitsperpixel = bitsperpixel;
        this.imagedescriptor = imagedescriptor;
        this.data = data;
    }

    public class TGABuilder {
        private byte idlength;
        private byte colourmaptype;
        private byte datatypecode;
        private short colourmaporigin;
        private short colourmaplength;
        private byte colourmapdepth;
        private short xOrigin;
        private short yOrigin;
        private short width;
        private short height;
        private byte bitsperpixel;
        private byte imagedescriptor;
        private byte[] data;

        public TGABuilder idlength(byte idlength) {
            this.idlength = idlength;
            return this;
        }

        public TGABuilder colourmaptype(byte colourmaptype) {
            this.colourmaptype = colourmaptype;
            return this;
        }

        public TGABuilder datatypecode(byte datatypecode) {
            this.datatypecode = datatypecode;
            return this;
        }

        public TGABuilder colourmaporigin(short colourmaporigin) {
            this.colourmaporigin = colourmaporigin;
            return this;
        }

        public TGABuilder colourmaplength(short colourmaplength) {
            this.colourmaplength = colourmaplength;
            return this;
        }

        public TGABuilder colourmapdepth(byte colourmapdepth) {
            this.colourmapdepth = colourmapdepth;
            return this;
        }

        public TGABuilder xOrigin(short xOrigin) {
            this.xOrigin = xOrigin;
            return this;
        }

        public TGABuilder yOrigin(short yOrigin) {
            this.yOrigin = yOrigin;
            return this;
        }

        public TGABuilder width(short width) {
            this.width = width;
            return this;
        }

        public TGABuilder height(short height) {
            this.height = height;
            return this;
        }

        public TGABuilder bitsPerPixel(byte bitsperpixel) {
            this.bitsperpixel = bitsperpixel;
            return this;
        }

        public TGABuilder imageDescriptor(byte imagedescriptor) {
            this.imagedescriptor = imagedescriptor;
            return this;
        }

        public TGABuilder data(byte[] data) {
            this.data = data;
            return this;
        }

        public TGA createTGA() {
            return new TGA(idlength, colourmaptype, datatypecode, colourmaporigin, colourmaplength, colourmapdepth, xOrigin, yOrigin, width, height, bitsperpixel, imagedescriptor, data);
        }
    }

    @Override
    public String toString() {
        return "TGA{" +
                "idlength=" + idlength +
                ", colourmaptype=" + colourmaptype +
                ", datatypecode=" + datatypecode +
                ", colourmaporigin=" + colourmaporigin +
                ", colourmaplength=" + colourmaplength +
                ", colourmapdepth=" + colourmapdepth +
                ", x_origin=" + x_origin +
                ", y_origin=" + y_origin +
                ", width=" + width +
                ", height=" + height +
                ", bitsperpixel=" + bitsperpixel +
                ", imagedescriptor=" + imagedescriptor +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
