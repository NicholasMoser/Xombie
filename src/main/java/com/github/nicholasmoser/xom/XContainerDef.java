package com.github.nicholasmoser.xom;

import java.util.List;
import java.util.Map;

/**
 * A class representing the definition of an XContainer, but containing no actual XContainer data.
 */
public class XContainerDef {
    private final String name;
    private final String value;
    private final List<XContainerDef> children;
    private final String guid;
    private final int Xver;
    private final boolean NoCntr;
    private final String id;
    private final String Xtype;
    private final boolean Xpack;
    private final String href;
    private final Map<String, ValueType> valueAttrs;

    public XContainerDef(String name, String value, List<XContainerDef> children, String guid, int xver, boolean noCntr, String id, String xtype, boolean xpack, String href, Map<String, ValueType> valueAttrs) {
        this.name = name;
        this.value = value;
        this.children = children;
        this.guid = guid;
        this.Xver = xver;
        this.NoCntr = noCntr;
        this.id = id;
        this.Xtype = xtype;
        this.Xpack = xpack;
        this.valueAttrs = valueAttrs;
        this.href = href;
    }

    public boolean isNoCntr() {
        return NoCntr;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public List<XContainerDef> getChildren() {
        return children;
    }

    public String getGuid() {
        return guid;
    }

    public int getXver() {
        return Xver;
    }

    public String getId() {
        return id;
    }

    public String getXtype() {
        return Xtype;
    }

    public boolean isXpack() {
        return Xpack;
    }

    public Map<String, ValueType> getValueAttrs() {
        return valueAttrs;
    }

    public String getHref() {
        return href;
    }
}