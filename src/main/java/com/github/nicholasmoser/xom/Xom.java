package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.xom.ctnr.XContainer;

import java.util.List;

/**
 * A record representing an entire xom file.
 *
 * @param header The header information of the xom file.
 * @param types The types of values.
 * @param schmType The schema type.
 * @param stringTable The table of XStrings.
 * @param containers The list of XContainers.
 */
public record Xom (XomHeader header,
                   List<XomType> types,
                   int schmType,
                   StringTable stringTable,
                   List<XContainer> containers)
{
}
