package com.github.nicholasmoser.xom;

import java.util.List;
import java.util.Map;

/**
 * A table of all of the XStrings in a xom file.
 *
 * @param sizeStrs The number of XStrings in the string table.
 * @param lenStrs The length of all of the XStrings in the string table in bytes.
 * @param offsets The list of offsets into the string table.
 * @param offsetToStr A mapping of offsets into the string table to the respective XString.
 */
public record StringTable (int sizeStrs,
                           int lenStrs,
                           List<Integer> offsets,
                           Map<Integer, String> offsetToStr){
    public String getString(int offset) {
        Integer mapOffset = offsets.get(offset);
        return offsetToStr.get(mapOffset);
    }
}
