package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.GUID;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class XomParser {
    private static final int TYPE_HEADER_SIZE = 0x40;
    private static final String MAGIC = "MOIK";
    private static final String TYPE = "TYPE";

    public static Xom parse(Path xomFile) throws IOException {
        byte[] bytes = Files.readAllBytes(xomFile);
        ByteStream bs = new ByteStream(bytes);
        XomHeader xomHeader = readXomHeader(bs);
        List<XomType> xomTypes = readTypeList(bs, xomHeader);
        return new Xom(xomHeader, xomTypes);
    }

    private static List<XomType> readTypeList(ByteStream bs, XomHeader xomHeader) throws IOException {
        List<XomType> xomTypes = new ArrayList<>(xomHeader.numberOfTypes());
        for (int i = 0; i < xomHeader.numberOfTypes(); i++) {
            int start = bs.offset();
            byte[] bytes = bs.readNBytes(4);
            if (!TYPE.equals(new String(bytes))) {
                throw new IOException("Xom type does not start with word TYPE at offset " + (bs.offset() - 4));
            }
            int subType = (bs.readWord() >> 0x18) & 0xFF;
            int size = (bs.readWord() >> 0x10) & 0xFFFF;
            bs.skipWord(); // padding
            byte[] guidBytes = bs.readNBytes(0x10);
            String guid = GUID.bytesToString(guidBytes);
            String name = bs.readCString();
            bs.seek(start + TYPE_HEADER_SIZE);
            xomTypes.add(new XomType(subType, size, guid, name));
        }
        return xomTypes;
    }

    private static XomHeader readXomHeader(ByteStream bs) throws IOException {
        byte[] magic = bs.readNBytes(4);
        if (!MAGIC.equals(new String(magic))) {
            throw new IOException("Invalid xom, missing magic word " + MAGIC);
        }
        int flag = bs.readWord(); // 1 or 2
        if (bs.skip(0x10) != 0x10) {
            throw new IOException("Failed to skip first header padding");
        }
        int numberOfTypes = (bs.readWord() >> 0x18) & 0xFF;
        int maxCount = (bs.readWord() >> 0x18) & 0xFF;
        int rootCount = (bs.readWord() >> 0x18) & 0xFF;
        if (bs.skip(0x1C) != 0x1C) {
            throw new IOException("Failed to skip second header padding");
        }
        return new XomHeader(flag, numberOfTypes, maxCount, rootCount);
    }
}
