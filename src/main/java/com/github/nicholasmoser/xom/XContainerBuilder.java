package com.github.nicholasmoser.xom;

import java.util.List;
import java.util.Map;

public class XContainerBuilder {
    private String name;
    private String value;
    private List<XContainer> children;
    private String guid;
    private int xver;
    private boolean noCntr;
    private String id;
    private String xtype;
    private boolean xpack;
    private String href;
    private Map<String, ValueType> valueAttrs;

    public XContainerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public XContainerBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public XContainerBuilder setChildren(List<XContainer> children) {
        this.children = children;
        return this;
    }

    public XContainerBuilder setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public XContainerBuilder setXver(int xver) {
        this.xver = xver;
        return this;
    }

    public XContainerBuilder setNoCntr(boolean noCntr) {
        this.noCntr = noCntr;
        return this;
    }

    public XContainerBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public XContainerBuilder setXtype(String xtype) {
        this.xtype = xtype;
        return this;
    }

    public XContainerBuilder setXpack(boolean xpack) {
        this.xpack = xpack;
        return this;
    }

    public XContainerBuilder setValueAttrs(Map<String, ValueType> valueAttrs) {
        this.valueAttrs = valueAttrs;
        return this;
    }

    public XContainerBuilder setHref(String href) {
        this.href = href;
        return this;
    }

    public XContainer createXContainer() {
        return new XContainer(name, value, children, guid, xver, noCntr, id, xtype, xpack, href, valueAttrs);
    }
}