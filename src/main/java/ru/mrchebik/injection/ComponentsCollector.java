package ru.mrchebik.injection;

import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import ru.mrchebik.gui.place.CellStageHelper;

import java.nio.file.Path;

public class ComponentsCollector {
    public static TextArea outputArea;
    public static TabPane tabPane;
    public static TreeView<Path> treeView;

    public static CellStageHelper createFilePlace;
    public static CellStageHelper createFolderPlace;
    public static CellStageHelper renameFilePlace;
    public static CellStageHelper renameFolderPlace;

    private ComponentsCollector() {
    }

    public static void setComponents(TextArea outputArea,
                                     TabPane tabPane,
                                     TreeView<Path> treeView,
                                     CellStageHelper createFilePlace,
                                     CellStageHelper createFolderPlace,
                                     CellStageHelper renameFilePlace,
                                     CellStageHelper renameFolderPlace) {
        ComponentsCollector.outputArea = outputArea;
        ComponentsCollector.tabPane = tabPane;
        ComponentsCollector.treeView = treeView;

        ComponentsCollector.createFilePlace = createFilePlace;
        ComponentsCollector.createFolderPlace = createFolderPlace;
        ComponentsCollector.renameFilePlace = renameFilePlace;
        ComponentsCollector.renameFolderPlace = renameFolderPlace;
    }
}
