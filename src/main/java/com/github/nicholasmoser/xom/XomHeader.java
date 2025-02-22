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
 */
public class XomHeader {

    private final int flag;
    private final int rootIndex;
    private int containerCount;
    private int numberOfTypes;

    /**
     * @param flag           The flag used to determine if the xom has a GUID field
     * @param numberOfTypes  The number of different types used in this xom
     * @param containerCount Total number of XContainers.
     * @param rootIndex      The index of the root element.
     */
    public XomHeader(int flag, int numberOfTypes, int containerCount, int rootIndex) {
        this.flag = flag;
        this.numberOfTypes = numberOfTypes;
        this.containerCount = containerCount;
        this.rootIndex = rootIndex;
    }

    public int flag() {
        return flag;
    }

    public int rootIndex() {
        return rootIndex;
    }

    public int containerCount() {
        return containerCount;
    }

    public int numberOfTypes() {
        return numberOfTypes;
    }

    public void decrementContainerCount() {
        containerCount -= 1;
    }

    public void decrementNumberOfTypes() {
        numberOfTypes -= 1;
    }
}
