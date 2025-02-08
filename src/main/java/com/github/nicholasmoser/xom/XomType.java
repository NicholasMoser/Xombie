package com.github.nicholasmoser.xom;

/**
 * A type for a value stored in the xom file. Subtype is always 0x0 or 0x20.
 *
 * @param subType
 * @param size
 * @param guid
 * @param name
 */
public record XomType(int subType, // 0x4
                      int size,    // 0x8
                      String guid, // 0x10
                      String name  // 0x20
) {
};
