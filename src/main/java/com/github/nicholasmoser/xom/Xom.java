package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.xom.ctnr.Container;

import java.util.List;

public record Xom (XomHeader header,
                   List<XomType> types,
                   int schmType,
                   StringTable stringTable,
                   List<Container> containers)
{
}
