package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.utils.ByteUtils;
import com.github.nicholasmoser.utils.GUID;
import com.github.nicholasmoser.xom.ctnr.XContainer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class XomWriter {
    private static final String MAGIC = "MOIK";
    private static final String TYPE = "TYPE";
    private static final String GUID_STR = "GUID";
    private static final String SCHM = "SCHM";
    private static final String STRS = "STRS";

    public static void write(Xom xom, RandomAccessFile raf) throws IOException {
        // Get xom data
        XomHeader header = xom.header();
        List<XomType> types = xom.types();
        int schmType = xom.schmType();
        StringTable stringTable = xom.stringTable();
        List<XContainer> containers = xom.containers();

        // Write xom data
        writeXomHeader(header, raf);
        writeTypes(types, raf);
        if (header.flag() == 0x2) {
            writeGUID(raf);
        }
        writeSchm(schmType, raf);
        writeStringTable(stringTable, raf);
        writeContainers(containers, raf);
    }

    private static void writeContainers(List<XContainer> containers, RandomAccessFile raf) {
        for (XContainer container : containers) {

        }
    }

    private static void writeStringTable(StringTable stringTable, RandomAccessFile raf) throws IOException {
        raf.write(STRS.getBytes(StandardCharsets.UTF_8));
        raf.write(ByteUtils.fromUint32LE(stringTable.sizeStrs()));
        raf.write(ByteUtils.fromUint32LE(stringTable.lenStrs()));
        for (Integer offset : stringTable.offsets()) {
            raf.write(ByteUtils.fromUint32LE(offset));
        }
        for (String text : stringTable.offsetToStr().values()) {
            raf.write(text.getBytes(StandardCharsets.UTF_8));
            raf.write(0);
        }
    }

    private static void writeSchm(int schmType, RandomAccessFile raf) throws IOException {
        raf.write(SCHM.getBytes(StandardCharsets.UTF_8));
        raf.write(ByteUtils.fromUint32LE(schmType));
        raf.write(new byte[0x8]);
    }

    private static void writeGUID(RandomAccessFile raf) throws IOException {
        raf.write(GUID_STR.getBytes(StandardCharsets.UTF_8));
        raf.write(new byte[0xC]);
    }

    private static void writeTypes(List<XomType> types, RandomAccessFile raf) throws IOException {
        for (XomType type : types) {
            raf.write(TYPE.getBytes(StandardCharsets.UTF_8));
            raf.write(ByteUtils.fromUint32LE(type.subType()));
            raf.write(ByteUtils.fromUint32LE(type.size()));
            raf.write(new byte[0x4]);
            raf.write(GUID.stringToBytes(type.guid()));
            raf.write(type.name().getBytes(StandardCharsets.UTF_8));
            ByteUtils.alignWrite(raf, 0x20);
        }
    }

    private static void writeXomHeader(XomHeader header, RandomAccessFile raf) throws IOException {
        raf.write(MAGIC.getBytes(StandardCharsets.UTF_8));
        raf.write(ByteUtils.fromUint32(header.flag()));
        raf.write(new byte[0x10]);
        raf.write(ByteUtils.fromUint32LE(header.numberOfTypes()));
        raf.write(ByteUtils.fromUint32LE(header.maxCount()));
        raf.write(ByteUtils.fromUint32LE(header.rootCount()));
        raf.write(new byte[0x1C]);
    }
}
