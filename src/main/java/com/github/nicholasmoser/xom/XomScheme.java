package com.github.nicholasmoser.xom;

import com.google.common.collect.Sets;
import de.pdark.decentxml.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class XomScheme {
    private static final Set<String> NON_VALUE_ATTRS = Sets.newHashSet("guid", "Xver", "NoCntr", "id", "Xtype", "Xpack");
    private static List<XContainerDef> CONTAINER_DEFINITIONS;
    private static Map<String, XContainerDef> CONTAINER_NAME_MAP;

    /**
     * @return The full list of all XContainer definitions.
     * @throws IOException If any I/O exception occurs
     */
    public static List<XContainerDef> getContainerDefinitions() throws IOException {
        if (CONTAINER_DEFINITIONS != null) {
            return CONTAINER_DEFINITIONS;
        }
        try (InputStream is = XomScheme.class.getResourceAsStream("XOMSCHM.xml")) {
            if (is == null) {
                throw new IOException("Missing XOMSCHM.xml");
            }
            byte[] bytes = is.readAllBytes();
            String text = new String(bytes);
            // check for byte order mark EFBBBF and exclude it from the String if it exists
            // https://en.wikipedia.org/wiki/Byte_order_mark
            if (text.startsWith("\uFEFF")) {
                text = text.substring(1);
            }
            XMLParser decentXMLParser = new XMLParser();
            de.pdark.decentxml.Document doc = decentXMLParser.parse(new XMLStringSource(text));
            Element root = doc.getRootElement();
            CONTAINER_DEFINITIONS = Collections.unmodifiableList(getChildren(root.getChildren(), null));
            return CONTAINER_DEFINITIONS;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * @return The mapping of XContainer names to XContainer definitions.
     * @throws IOException If any I/O exception occurs
     */
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

    /**
     * Gets the children of a class name, used to lookup XContainer definition children of a parent class.
     *
     * @param parentClass The name of the parent class.
     * @return The XContainer definition children of the class.
     * @throws IOException If any I/O exception occurs
     */
    public static List<XContainerDef> getParentClassChildren(String parentClass) throws IOException {
        if (parentClass == null) {
            return Collections.emptyList();
        }
        List<XContainerDef> values = new ArrayList<>();
        Map<String, XContainerDef> containerNameMap = getContainerNameMap();
        XContainerDef parent = containerNameMap.get(parentClass);
        if (parent != null) {
            for (XContainerDef def : parent.children()) {
                if (!"XRef".equals(def.id())) {
                    // Don't add XReferences, just add their Values
                    values.add(def);
                }
            }
            values.addAll(getParentClassChildren(parent.parentClass()));
        }
        return values;
    }

    /**
     * Gets the children of a class name, used to lookup XContainer definition children of a parent class. This will
     * only return children BEFORE the current class.
     *
     * @param parentClass The name of the parent class.
     * @return The XContainer definition children of the class.
     * @throws IOException If any I/O exception occurs
     */
    public static List<XContainerDef> getParentClassChildrenBefore(String currentClass, String parentClass) throws IOException {
        if (parentClass == null) {
            return Collections.emptyList();
        }
        List<XContainerDef> values = new ArrayList<>();
        Map<String, XContainerDef> containerNameMap = getContainerNameMap();
        XContainerDef parent = containerNameMap.get(parentClass);
        if (parent != null) {
            for (XContainerDef def : parent.children()) {
                if (currentClass.equals(def.name())) {
                    return values; // We've hit the current class, exit now
                }
                if (!"XRef".equals(def.id())) {
                    // Don't add XReferences, just add their Values
                    values.add(def);
                }
            }
            values.addAll(getParentClassChildren(parent.parentClass()));
        }
        return values;
    }

    /**
     * Gets the children of a class name, used to lookup XContainer definition children of a parent class. This will
     * only return children AFTER the current class.
     *
     * @param parentClass The name of the parent class.
     * @return The XContainer definition children of the class.
     * @throws IOException If any I/O exception occurs
     */
    public static List<XContainerDef> getParentClassChildrenAfter(String currentClass, String parentClass) throws IOException {
        if (parentClass == null) {
            return Collections.emptyList();
        }
        boolean start = false;
        List<XContainerDef> values = new ArrayList<>();
        Map<String, XContainerDef> containerNameMap = getContainerNameMap();
        XContainerDef parent = containerNameMap.get(parentClass);
        if (parent != null) {
            for (XContainerDef def : parent.children()) {
                if (currentClass.equals(def.name())) {
                    start = true; // We've hit the current class, begin actually adding children
                }
                if (start && !"XRef".equals(def.id())) {
                    // Don't add XReferences, just add their Values
                    values.add(def);
                }
            }
            values.addAll(getParentClassChildren(parent.parentClass()));
        }
        return values;
    }

    private static void getContainerNameMap(List<XContainerDef> xContainerDefs, Map<String, XContainerDef> names) {

        for (XContainerDef xContainerDef : xContainerDefs) {
            if (names.containsKey(xContainerDef.name())) {
                throw new IllegalStateException("Duplicate name: " + xContainerDef.name());
            }
            if (xContainerDef.guid() != null) {
                // only insert if there's a GUID
                names.put(xContainerDef.name(), xContainerDef);
                // Add shortened versions of names
                if ("OccludingCameraPropertiesContainer".equals(xContainerDef.name())) {
                    names.put("OccludingCameraPropertiesContai", xContainerDef);
                }
            }
            getContainerNameMap(xContainerDef.children(), names);
        };
    }

    private static XContainerDef getXContainer(Element node, String parentClass) {
        XContainerDefBuilder xContainer = new XContainerDefBuilder();
        xContainer.setName(node.getName());
        xContainer.setValue(getValueText(node));

        // Handle parent
        xContainer.setParentClass(parentClass);

        // Handle attributes
        LinkedHashMap<String, ValueType> valueAttributes = new LinkedHashMap<>();
        List<Attribute> attrs = node.getAttributes();
        for (Attribute attr : attrs) {
            String name = attr.getName();
            if (!NON_VALUE_ATTRS.contains(name)) {
                ValueType value = ValueType.get(attr.getValue());
                // If the href is a specific container, just put down XContainer
                valueAttributes.put(name, Objects.requireNonNullElse(value, ValueType.XContainer));
            }
        }
        String xTypeValue = null; // need to see if this is a class or not
        xContainer.setValueAttrs(valueAttributes);
        Attribute guid = getNamedItem(attrs, "guid");
        if (guid != null) {
            xContainer.setGuid(guid.getValue());
        }
        Attribute xVer = getNamedItem(attrs, "Xver");
        if (xVer != null) {
            xContainer.setXver(Integer.parseInt(xVer.getValue()));
        }
        Attribute noCntr = getNamedItem(attrs, "NoCntr");
        if (noCntr != null) {
            xContainer.setNoCntr(Boolean.parseBoolean(noCntr.getValue()));
        }
        Attribute id = getNamedItem(attrs, "id");
        if (id != null) {
            xContainer.setId(id.getValue());
        }
        Attribute xType = getNamedItem(attrs, "Xtype");
        if (xType != null) {
            xTypeValue = xType.getValue();
            xContainer.setXtype(xTypeValue);
        }
        Attribute xPack = getNamedItem(attrs, "Xpack");
        if (xPack != null) {
            xContainer.setXpack(Boolean.parseBoolean(xPack.getValue()));
        }

        // Handle children, propagate class name if it's a class for children to use
        boolean isParentClass = "XClass".equals(xTypeValue) && !"XContainer".equals(node.getName());
        String className = isParentClass ? node.getName() : null;
        xContainer.setChildren(getChildren(node.getChildren(), className));

        return xContainer.createXContainer();
    }

    private static Attribute getNamedItem(List<Attribute> attrs, String name) {
        for (Attribute attr : attrs) {
            if (name.equals(attr.getName()))
                return attr;
        }
        return null;
    }

    private static List<XContainerDef> getChildren(List<Element> children, String parentClass) {
        List<XContainerDef> xContainerDefs = new ArrayList<>(children.size());
        for (Element child : children) {
            if (child.getType() == XMLTokenizer.Type.ELEMENT) {
                xContainerDefs.add(getXContainer(child, parentClass));
            }
        }
        return xContainerDefs;
    }

    private static String getValueText(Element node) {
        if (node.getType() == XMLTokenizer.Type.ELEMENT) {
            String text = node.getText();
            return text.isBlank() ? null : text;
        } else {
            return null;
        }
    }
}
