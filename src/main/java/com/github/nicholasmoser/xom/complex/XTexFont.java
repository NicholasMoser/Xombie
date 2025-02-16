package com.github.nicholasmoser.xom.complex;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.xom.StringTable;
import com.github.nicholasmoser.xom.ctnr.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XTexFont implements XContainer {
    private List<Tuple> charCoords; // List of two coordinate tuples, e.g. (0.0, 0.0)
    private List<Tuple> charSizes; // List of two size tuples, e.g. (1.0, 1.0)
    private List<XUInt8> textureStages;
    private List<XUInt8> attributes;
    private XUInt flags;
    private XString name;

    private XTexFont(List<Tuple> charCoords, List<Tuple> charSizes, List<XUInt8> textureStages, List<XUInt8> attributes, XUInt flags, XString name) {
        this.charCoords = charCoords;
        this.charSizes = charSizes;
        this.textureStages = textureStages;
        this.attributes = attributes;
        this.flags = flags;
        this.name = name;
    }

    public static XTexFont read(ByteStream bs, StringTable stringTable) throws IOException {
        int charCoordsSize = bs.readVarint();
        List<Tuple> charCoords = new ArrayList<>(charCoordsSize);
        for (int i = 0; i < charCoordsSize; i++) {
            List<Value> values = new ArrayList<>(2);
            values.add(XFloat.read("x", bs));
            values.add(XFloat.read("y", bs));
            charCoords.add(new Tuple("CharCoords", values));
        }
        int charSizesSize = bs.readVarint();
        List<Tuple> charSizes = new ArrayList<>(charSizesSize);
        for (int i = 0; i < charSizesSize; i++) {
            List<Value> values = new ArrayList<>(2);
            values.add(XFloat.read("x", bs));
            values.add(XFloat.read("y", bs));
            charSizes.add(new Tuple("CharSizes", values));
        }
        int textureStagesSize = bs.readVarint();
        List<XUInt8> textureStages = new ArrayList<>(textureStagesSize);
        for (int i = 0; i < textureStagesSize; i++) {
            textureStages.add(XUInt8.read("TextureStages", bs));
        }
        int attributesSize = bs.readVarint();
        List<XUInt8> attributes = new ArrayList<>(attributesSize);
        for (int i = 0; i < attributesSize; i++) {
            attributes.add(XUInt8.read("Attributes", bs));
        }
        XUInt flags = XUInt.read("Flags", bs);
        XString name = XString.read("Name", bs, stringTable);

        return new XTexFont(charCoords, charSizes, textureStages, attributes, flags, name);
    }

    @Override
    public String toString() {
        return "XTexFont{" +
                "charCoords=" + charCoords +
                ", charSizes=" + charSizes +
                ", textureStages=" + textureStages +
                ", attributes=" + attributes +
                ", flags=" + flags +
                ", name=" + name +
                '}';
    }
}
