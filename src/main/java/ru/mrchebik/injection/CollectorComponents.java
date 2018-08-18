package ru.mrchebik.injection;

import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;

import java.nio.file.Path;

public class CollectorComponents {
    public static TextArea outputArea;
    public static TabPane tabPane;
    public static TreeView<Path> treeView;

    private CollectorComponents() {
    }

    public static void setComponents(TextArea outputArea,
                                     TabPane tabPane,
                                     TreeView<Path> treeView) {
        CollectorComponents.outputArea = outputArea;
        CollectorComponents.tabPane = tabPane;
        CollectorComponents.treeView = treeView;
    }
}
