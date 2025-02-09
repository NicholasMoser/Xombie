package com.github.nicholasmoser.xom;

import com.google.common.collect.Sets;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class XomScheme {
    private static final Set<String> NON_VALUE_ATTRS = Sets.newHashSet("guid", "Xver", "NoCntr", "id", "Xtype", "Xpack", "href");
    private static List<XContainerDef> CONTAINER_DEFINITIONS;
    private static Map<String, XContainerDef> CONTAINER_NAME_MAP;

    public static List<XContainerDef> getContainerDefinitions() throws IOException {
        if (CONTAINER_DEFINITIONS != null) {
            return CONTAINER_DEFINITIONS;
        }
        try (InputStream is = XomScheme.class.getResourceAsStream("XOMSCHM.xml")) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            doc.normalizeDocument();
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();
            CONTAINER_DEFINITIONS = Collections.unmodifiableList(getChildren(root.getChildNodes()));
            return CONTAINER_DEFINITIONS;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static Map<String, XContainerDef> getContainerNameMap() throws IOException {
        if (CONTAINER_NAME_MAP != null) {
            return CONTAINER_NAME_MAP;
        }
        List<XContainerDef> xContainerDefs = getContainerDefinitions();
        Map<String, XContainerDef> names = new HashMap<>();
        getContainerNameMap(xContainerDefs, names);
        CONTAINER_NAME_MAP = Collections.unmodifiableMap(names);
        return CONTAINER_NAME_MAP;
    }

    private static void getContainerNameMap(List<XContainerDef> xContainerDefs, Map<String, XContainerDef> names) {

        for (XContainerDef xContainerDef : xContainerDefs) {
            if (names.containsKey(xContainerDef.getName())) {
                throw new IllegalStateException("Duplicate name: " + xContainerDef.getName());
            }
            if (xContainerDef.getGuid() != null) {
                // only insert if there's a GUID
                names.put(xContainerDef.getName(), xContainerDef);
            }
            getContainerNameMap(xContainerDef.getChildren(), names);
        };
    }

    private static XContainerDef getXContainer(Node node) {
        XContainerDefBuilder xContainer = new XContainerDefBuilder();
        xContainer.setName(node.getNodeName());
        xContainer.setValue(getValueText(node));

        // Handle children
        xContainer.setChildren(getChildren(node.getChildNodes()));

        // Handle attributes
        TreeMap<String, ValueType> valueAttributes = new TreeMap<>();
        NamedNodeMap attrs = node.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            String name = attr.getNodeName();
            if (!NON_VALUE_ATTRS.contains(name)) {
                ValueType value = ValueType.valueOf(attr.getNodeValue());
                valueAttributes.put(name, value);
            }
        }
        xContainer.setValueAttrs(valueAttributes);
        Node guid = attrs.getNamedItem("guid");
        if (guid != null) {
            xContainer.setGuid(guid.getNodeValue());
        }
        Node xVer = attrs.getNamedItem("Xver");
        if (xVer != null) {
            xContainer.setXver(Integer.parseInt(xVer.getNodeValue()));
        }
        Node noCntr = attrs.getNamedItem("NoCntr");
        if (noCntr != null) {
            xContainer.setNoCntr(Boolean.parseBoolean(noCntr.getNodeValue()));
        }
        Node id = attrs.getNamedItem("id");
        if (id != null) {
            xContainer.setId(id.getNodeValue());
        }
        Node xType = attrs.getNamedItem("Xtype");
        if (xType != null) {
            xContainer.setXtype(xType.getNodeValue());
        }
        Node xPack = attrs.getNamedItem("Xpack");
        if (xPack != null) {
            xContainer.setXpack(Boolean.parseBoolean(xPack.getNodeValue()));
        }
        Node href = attrs.getNamedItem("href");
        if (href != null) {
            xContainer.setHref(href.getNodeValue());
        }
        return xContainer.createXContainer();
    }

    private static List<XContainerDef> getChildren(NodeList children) {
        List<XContainerDef> xContainerDefs = new ArrayList<>(children.getLength());
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                xContainerDefs.add(getXContainer(child));
            }
        }
        return xContainerDefs;
    }

    private static String getValueText(Node node) {
        NodeList children = node.getChildNodes();
        if (children.getLength() != 1) {
            return null;
        }
        Node child = children.item(0);
        if (child.getNodeType() == Node.TEXT_NODE) {
            return child.getNodeValue();
        } else {
            return null;
        }
    }
}
