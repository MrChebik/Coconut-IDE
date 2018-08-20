package ru.mrchebik.gui.place.work.event.structure;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import ru.mrchebik.gui.node.treeCell.CustomTreeCell;

import java.nio.file.Path;

public class StructureUpdateGraphic implements Callback<TreeView<Path>, TreeCell<Path>> {
    @Override
    public TreeCell<Path> call(TreeView<Path> param) {
        return new CustomTreeCell();
    }
}
