package ru.mrchebik.gui.places.work.event.structure;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.mrchebik.gui.places.creator.object.ObjectPlace;
import ru.mrchebik.model.CommandPath;
import ru.mrchebik.model.Project;

import java.nio.file.Path;

/**
 * Created by mrchebik on 9/15/17.
 */
@RequiredArgsConstructor
public class StructureUpdateGraphic implements Callback<TreeView<Path>, TreeCell<Path>> {
    @NonNull private Project project;
    @NonNull private ObjectPlace objectPlace;
    @NonNull private CommandPath commandPath;

    @Override
    public TreeCell<Path> call(TreeView<Path> param) {
        return new CustomTreeCell(project, objectPlace, commandPath);
    }
}
