package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.utils.ByteStream;
import com.github.nicholasmoser.utils.GUID;
import com.github.nicholasmoser.xom.ctnr.XContainer;
import com.github.nicholasmoser.xom.ctnr.XContainerGeneric;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XomParser {
    private static final int TYPE_HEADER_SIZE = 0x40;
    private static final String MAGIC = "MOIK";
    private static final String TYPE = "TYPE";
    private static final String GUID_STR = "GUID";
    private static final String SCHM = "SCHM";
    private static final String STRS = "STRS";
    private static final String CTNR = "CTNR";

    public static Xom parse(Path xomFile) throws IOException {
        byte[] bytes = Files.readAllBytes(xomFile);
        ByteStream bs = new ByteStream(bytes);
        XomHeader xomHeader = readXomHeader(bs);
        List<XomType> xomTypes = readTypeList(bs, xomHeader);
        if (xomHeader.flag() == 0x2) {
            skipGUID(bs);
        }
        if (!SCHM.equals(new String(bs.readNBytes(4)))) {
            throw new IOException("Failed to read SCHM field in xom header at offset " + (bs.offset() - 4));
        }
        int schmType = bs.readLEWord();
        bs.skipNBytes(0x8);
        StringTable stringTable = readStrTable(bs);
        List<XContainer> containers = getContainers(bs, xomTypes, stringTable);
        return new Xom(xomHeader, xomTypes, schmType, stringTable, containers);
    }

    private static List<XContainer> getContainers(ByteStream bs, List<XomType> types, StringTable stringTable) throws IOException {
        List<XContainer> containers = new ArrayList<>();
        if (!bs.bytesAreLeft()) {
            return containers;
        }
        for (XomType type : types) {
            for (int i = 0; i < type.size(); i++) {
                byte[] peekBytes = bs.peekBytes(4);
                if (CTNR.equals(new String(peekBytes))) {
                    // CTNR is apparently optional after the string table?
                    bs.skipWord();
                }
                XContainer container = XContainerGeneric.read(bs, type, types, stringTable);
                containers.add(container);
            }
        }
        return containers;
    }

    private static StringTable readStrTable(ByteStream bs) throws IOException {
        if (!STRS.equals(new String(bs.readNBytes(4)))) {
            throw new IOException("Failed to read STRS field in xom header at offset " + (bs.offset() - 4));
        }
        int sizeStrs = bs.readLEWord();
        int lenStrs = bs.readLEWord();
        List<Integer> offsets = new ArrayList<>(sizeStrs);
        for (int i = 0; i < sizeStrs; i++) {
            offsets.add(bs.readLEWord());
        }
        Map<Integer, String> offsetToStr = new LinkedHashMap<>();
        offsetToStr.put(0, "");
        int start = bs.offset();
        for (int i = 0; i < sizeStrs; i++) {
            int current = bs.offset() - start;
            String str = bs.readCString();
            offsetToStr.put(current, str);
        }
        return new StringTable(sizeStrs, lenStrs, offsets, offsetToStr);
    }

    private static void skipGUID(ByteStream bs) throws IOException {
        // skip 0x10 byte GUID field
        byte[] guidBytes = bs.readNBytes(4);
        if (!GUID_STR.equals(new String(guidBytes))) {
            throw new IOException("Failed to read GUID field in xom header at offset " + (bs.offset() - 4));
        }
        bs.skipNBytes(0xC);
    }

    private static List<XomType> readTypeList(ByteStream bs, XomHeader xomHeader) throws IOException {
        List<XomType> xomTypes = new ArrayList<>(xomHeader.numberOfTypes());
        for (int i = 0; i < xomHeader.numberOfTypes(); i++) {
            int start = bs.offset();
            byte[] bytes = bs.readNBytes(4);
            if (!TYPE.equals(new String(bytes))) {
                throw new IOException("Xom type does not start with word TYPE at offset " + (bs.offset() - 4));
            }
            int subType = bs.readLEWord();
            int size = bs.readLEWord();
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
        bs.skipNBytes(0x10);
        int numberOfTypes = bs.readLEWord();
        int maxCount = bs.readLEWord();
        int rootCount = bs.readLEWord();
        bs.skipNBytes(0x1C);
        return new XomHeader(flag, numberOfTypes, maxCount, rootCount);
    }
}
