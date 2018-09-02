package ru.mrchebik.gui.collector;

import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;

import java.nio.file.Path;

public class ComponentsCollector {
    public static TextArea outputArea;
    public static TabPane tabPane;
    public static TreeView<Path> treeView;

    private ComponentsCollector() {
    }

    public static void setComponents(TextArea outputArea,
                                     TabPane tabPane,
                                     TreeView<Path> treeView) {
        ComponentsCollector.outputArea = outputArea;
        ComponentsCollector.tabPane = tabPane;
        ComponentsCollector.treeView = treeView;
    }
}
