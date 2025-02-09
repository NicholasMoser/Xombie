package com.github.nicholasmoser.xom;

import java.util.List;
import java.util.Map;

public class XContainerDefBuilder {
    private String name;
    private String value;
    private List<XContainerDef> children;
    private String guid;
    private int xver;
    private boolean noCntr;
    private String id;
    private String xtype;
    private boolean xpack;
    private String href;
    private Map<String, ValueType> valueAttrs;

    public XContainerDefBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public XContainerDefBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public XContainerDefBuilder setChildren(List<XContainerDef> children) {
        this.children = children;
        return this;
    }

    public XContainerDefBuilder setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public XContainerDefBuilder setXver(int xver) {
        this.xver = xver;
        return this;
    }

    public XContainerDefBuilder setNoCntr(boolean noCntr) {
        this.noCntr = noCntr;
        return this;
    }

    public XContainerDefBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public XContainerDefBuilder setXtype(String xtype) {
        this.xtype = xtype;
        return this;
    }

    public XContainerDefBuilder setXpack(boolean xpack) {
        this.xpack = xpack;
        return this;
    }

    public XContainerDefBuilder setValueAttrs(Map<String, ValueType> valueAttrs) {
        this.valueAttrs = valueAttrs;
        return this;
    }

    public XContainerDefBuilder setHref(String href) {
        this.href = href;
        return this;
    }

    public XContainerDef createXContainer() {
        return new XContainerDef(name, value, children, guid, xver, noCntr, id, xtype, xpack, href, valueAttrs);
    }
}