package com.github.nicholasmoser.xom;

import java.util.List;

public record Xom (XomHeader header,
                   List<XomType> types)
{
}
