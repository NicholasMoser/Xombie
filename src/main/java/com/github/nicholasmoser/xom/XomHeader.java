package com.github.nicholasmoser.xom;

/**
 * The first 0x40 bytes of the xom file. The first flag is usually set to 0x2, but is set to 0x1 for some test files,
 * specifically:
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
 * @param flag The flag likely used to determine if it's a debug file.
 * @param numberOfTypes
 * @param maxCount
 * @param rootCount
 */
public record XomHeader(int flag, // 0x4
                        int numberOfTypes, // 0x18
                        int maxCount, // 0x1C
                        int rootCount) // 0x20
{
}
