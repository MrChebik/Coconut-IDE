package ru.mrchebik.gui.place.work.event.structure;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import lombok.AllArgsConstructor;
import ru.mrchebik.gui.node.treeCell.CustomTreeCell;
import ru.mrchebik.model.ActionPlaces;
import ru.mrchebik.model.CommandPath;

import java.nio.file.Path;

@AllArgsConstructor
public class StructureUpdateGraphic implements Callback<TreeView<Path>, TreeCell<Path>> {
    private CommandPath commandPath;
    private ActionPlaces places;

    @Override
    public TreeCell<Path> call(TreeView<Path> param) {
        return new CustomTreeCell(commandPath, places);
    }
}
