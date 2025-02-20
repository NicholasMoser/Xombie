package com.github.nicholasmoser.xom;

import java.util.List;
import java.util.Map;

/**
 * A class representing the definition of an XContainer, but containing no actual XContainer data. This is read from the
 * schema definition file XOMSCHM.xml and is used to know how to read a specific XContainer.
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
    private final Map<String, ValueType> valueAttrs;
    private final String parentClass;

    public XContainerDef(String name, String value, List<XContainerDef> children, String guid, int xver, boolean noCntr, String id, String xtype, boolean xpack, Map<String, ValueType> valueAttrs, String parentClass) {
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
        this.parentClass = parentClass;
    }

    public boolean isNoCntr() {
        return NoCntr;
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    public List<XContainerDef> children() {
        return children;
    }

    public String guid() {
        return guid;
    }

    public int xVer() {
        return Xver;
    }

    public String id() {
        return id;
    }

    public String xType() {
        return Xtype;
    }

    public boolean xPack() {
        return Xpack;
    }

    public Map<String, ValueType> valueAttrs() {
        return valueAttrs;
    }

    public String parentClass() {
        return parentClass;
    }

    @Override
    public String toString() {
        return "XContainerDef{" +
                "name='" + name + '\'' +
                ", childrenLength=" + children.size() +
                ", guid='" + guid + '\'' +
                ", Xver=" + Xver +
                ", NoCntr=" + NoCntr +
                ", id='" + id + '\'' +
                ", Xtype='" + Xtype + '\'' +
                ", Xpack=" + Xpack +
                ", valueAttrs=" + valueAttrs +
                ", parentClass='" + parentClass + '\'' +
                '}';
    }
}