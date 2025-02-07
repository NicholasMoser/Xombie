package com.github.nicholasmoser.xom;

import com.google.common.collect.Sets;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class XomScheme {
    private static final Set<String> ALL_ATTRIBUTES = new HashSet<>();
    private static final Set<String> NON_VALUE_ATTRS = Sets.newHashSet("guid", "Xver", "NoCntr", "id", "Xtype", "Xpack", "href");

    public static Set<String> getAllAttributes() {
        return Collections.unmodifiableSet(ALL_ATTRIBUTES);
    }

    public static List<XContainer> get() throws IOException {
        try (InputStream is = XomScheme.class.getResourceAsStream("XOMSCHM.xml")) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            doc.normalizeDocument();
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();
            return getChildren(root.getChildNodes());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static void fillNameMap(List<XContainer> xContainers, Map<String, XContainer> names) {
        for (XContainer xContainer : xContainers) {
            if (names.containsKey(xContainer.getName())) {
                throw new IllegalStateException("Duplicate name: " + xContainer.getName());
            }
            if (xContainer.getGuid() != null) {
                // only insert if there's a GUID
                names.put(xContainer.getName(), xContainer);
            }
            fillNameMap(xContainer.getChildren(), names);
        }
    }

    private static XContainer getXContainer(Node node) {
        XContainerBuilder xContainer = new XContainerBuilder();
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
            ALL_ATTRIBUTES.add(name);
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

    private static List<XContainer> getChildren(NodeList children) {
        List<XContainer> xContainers = new ArrayList<>(children.getLength());
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                xContainers.add(getXContainer(child));
            }
        }
        return xContainers;
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
