package ru.mrchebik.injection;

import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import ru.mrchebik.gui.place.CellStageHelper;

import java.nio.file.Path;

public class CollectorComponents {
    public static TextArea outputArea;
    public static TabPane tabPane;
    public static TreeView<Path> treeView;

    public static CellStageHelper createFilePlace;
    public static CellStageHelper createFolderPlace;
    public static CellStageHelper renameFilePlace;
    public static CellStageHelper renameFolderPlace;

    private CollectorComponents() {
    }

    public static void setComponents(TextArea outputArea,
                                     TabPane tabPane,
                                     TreeView<Path> treeView,
                                     CellStageHelper createFilePlace,
                                     CellStageHelper createFolderPlace,
                                     CellStageHelper renameFilePlace,
                                     CellStageHelper renameFolderPlace) {
        CollectorComponents.outputArea = outputArea;
        CollectorComponents.tabPane = tabPane;
        CollectorComponents.treeView = treeView;

        CollectorComponents.createFilePlace = createFilePlace;
        CollectorComponents.createFolderPlace = createFolderPlace;
        CollectorComponents.renameFilePlace = renameFilePlace;
        CollectorComponents.renameFolderPlace = renameFolderPlace;
    }
}
