package ru.mrchebik.gui.collector;

import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import ru.mrchebik.gui.place.CellStageAction;

import java.nio.file.Path;

public class ComponentsCollector {
    public static TextArea outputArea;
    public static TabPane tabPane;
    public static TreeView<Path> treeView;

    public static CellStageAction createFilePlace;
    public static CellStageAction createFolderPlace;
    public static CellStageAction renameFilePlace;
    public static CellStageAction renameFolderPlace;

    private ComponentsCollector() {
    }

    public static void setComponents(TextArea outputArea,
                                     TabPane tabPane,
                                     TreeView<Path> treeView,
                                     CellStageAction createFilePlace,
                                     CellStageAction createFolderPlace,
                                     CellStageAction renameFilePlace,
                                     CellStageAction renameFolderPlace) {
        ComponentsCollector.outputArea = outputArea;
        ComponentsCollector.tabPane = tabPane;
        ComponentsCollector.treeView = treeView;

        ComponentsCollector.createFilePlace = createFilePlace;
        ComponentsCollector.createFolderPlace = createFolderPlace;
        ComponentsCollector.renameFilePlace = renameFilePlace;
        ComponentsCollector.renameFolderPlace = renameFolderPlace;
    }
}
