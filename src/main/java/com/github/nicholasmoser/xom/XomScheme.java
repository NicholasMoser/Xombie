package com.github.nicholasmoser.xom;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class XomScheme {
    public static void get() throws IOException {
        try (InputStream is = XomScheme.class.getResourceAsStream("XOMSCHM.xml")) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            doc.normalizeDocument();
            doc.getDocumentElement().normalize();

            XContainer xGraphSet = getXContainer(doc.getElementsByTagName("XGraphSet").item(0));
            XContainer xBaseResourceDescriptor = getXContainer(doc.getElementsByTagName("XBaseResourceDescriptor").item(0));
            XContainer xAnimClipLibrary = getXContainer(doc.getElementsByTagName("XAnimClipLibrary").item(0));
            XContainer xContainers = getXContainer(doc.getElementsByTagName("XContainer").item(0));
            System.out.println();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private static XContainer getXContainer(Node node) {
        String name = node.getNodeName();
        String value = getValueText(node);

        // Handle children
        List<XContainer> children = getChildren(node.getChildNodes());

        // Handle attributes
        NamedNodeMap attrs = node.getAttributes();
        Node guidNode = attrs.getNamedItem("guid");
        String guid = guidNode != null ? guidNode.getNodeValue() : null;
        Node XverNode = attrs.getNamedItem("Xver");
        int Xver = XverNode != null ? Integer.parseInt(XverNode.getNodeValue()) : 0;
        Node NoCntrNode = attrs.getNamedItem("NoCntr");
        boolean NoCntr = NoCntrNode != null && Boolean.parseBoolean(NoCntrNode.getNodeValue());
        Node idNode = attrs.getNamedItem("id");
        String id = idNode != null ? idNode.getNodeValue() : null;
        Node XtypeNode = attrs.getNamedItem("Xtype");
        String Xtype = XtypeNode != null ? XtypeNode.getNodeValue() : null;
        return new XContainer(name, value, children, guid, Xver, NoCntr, id, Xtype);
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
