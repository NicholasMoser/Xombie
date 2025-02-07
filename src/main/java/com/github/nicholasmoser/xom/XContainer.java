package com.github.nicholasmoser.xom;

import java.util.List;

public record XContainer(
        String name,
        String value,
        List<XContainer> children,
        String guid,
        int Xver,
        boolean NoCntr,
        String id,
        String Xtype
) {
};
