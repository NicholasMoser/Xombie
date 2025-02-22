package com.github.nicholasmoser.xom;

import com.github.nicholasmoser.xom.ctnr.*;
import com.google.common.collect.Maps;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

import java.util.List;
import java.util.Map;

public class XomViewerController {
    public Label leftStatus;
    public Label rightStatus;
    public TreeView<String> tree;
    public AnchorPane data;
    public Xom xom;
    public XomHeader header;
    public List<XContainer> containers;
    public Map<String, XContainer> idToContainer;

    public void init(Xom xom) {
        this.xom = xom;
        this.header = xom.header();
        this.containers = xom.containers();
        this.idToContainer = Maps.newHashMapWithExpectedSize(containers.size());
        // Create root
        int rootIndex = header.rootIndex();
        XContainer root = containers.get(rootIndex - 1);
        String rootId = getId(root.name(), rootIndex);
        TreeItem<String> rootItem = new TreeItem<>(rootId);
        rootItem.setExpanded(true);
        tree.setRoot(rootItem);
        idToContainer.put(rootId, root);
        // Create children
        addTreeChildren(root.values(), rootItem);
    }

    private void addTreeChildren(List<Value> values, TreeItem<String> item) {
        for (Value value : values) {
            if (value instanceof Ref ref) {
                int refValue = ref.value();
                if (refValue > 0) {
                    XContainer childContainer = containers.get(refValue - 1);
                    String id = getId(childContainer.name(), refValue);
                    TreeItem<String> child = new TreeItem<>(id);
                    item.getChildren().add(child);
                    addTreeChildren(childContainer.values(), child);
                } else {
                    TreeItem<String> child = new TreeItem<>(value.name());
                    item.getChildren().add(child);
                }
            } else if (value instanceof XCollection collection) {
                TreeItem<String> child = new TreeItem<>(collection.name());
                item.getChildren().add(child);
                addTreeChildren(collection.values(), child);
            } else if (value instanceof Tuple tuple) {
                TreeItem<String> child = new TreeItem<>(tuple.name());
                item.getChildren().add(child);
                addTreeChildren(tuple.values(), child);
            } else {
                TreeItem<String> child = new TreeItem<>(value.name());
                item.getChildren().add(child);
            }
        }
    }

    private static String getId(String name, int index) {
        return String.format("%s [%d]", name, index);
    }
}
