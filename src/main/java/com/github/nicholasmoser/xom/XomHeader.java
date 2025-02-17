package com.github.nicholasmoser.xom;

/**
 * The first 0x40 bytes of the xom file. The first flag is usually set to 0x2, but is set to 0x1 for some test files.
 * 0x2 means it has a 0x10 byte GUID field and 0x1 means it doesn't have it. The files with 0x1 are specifically:
 * <ul>
 *     <li>Maps/4Player.xom</li>
 *     <li>Maps/assaultcourse.xom</li>
 *     <li>Maps/cratetest.xom</li>
 *     <li>Maps/normaltest.xom</li>
 *     <li>Maps/pirate.xom</li>
 *     <li>Maps/piratetest.xom</li>
 *     <li>Maps/piratetest2.xom</li>
 *     <li>Maps/tennis.xom</li>
 *     <li>Maps/unitcube.xom</li>
 *     <li>Maps/VsCpu.xom</li>
 *     <li>Maps/w.xom</li>
 *     <li>Maps/wormisland.xom</li>
 * </ul>
 *
 * @param flag The flag used to determine if the xom has a GUID field
 * @param numberOfTypes The number of different types used in this xom
 * @param maxCount Total number of XContainers.
 * @param rootCount Number of XContainers attached to the root.
 */
public record XomHeader(int flag, // 0x4
                        int numberOfTypes, // 0x18
                        int maxCount, // 0x1C
                        int rootCount) // 0x20
{
}
