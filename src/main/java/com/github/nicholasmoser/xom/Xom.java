package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.xom.ctnr.XContainer;

import java.util.List;

public record Xom (XomHeader header,
                   List<XomType> types,
                   int schmType,
                   StringTable stringTable,
                   List<XContainer> containers)
{
}
