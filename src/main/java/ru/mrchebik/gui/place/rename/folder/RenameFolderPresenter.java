package ru.mrchebik.gui.place.rename.folder;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lombok.SneakyThrows;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.model.ActionPlaces;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;

public class RenameFolderPresenter extends KeyHelper {
    @FXML
    private TextField name;
    @Inject
    private ActionPlaces places;

    @FXML
    private void handleRename() {
        renameFile();
    }

    @FXML
    private void handleRenameWithKey(KeyEvent event) {
        if (isEnter(event))
            renameFile();
    }

    @SneakyThrows(IOException.class)
    private void renameFile() {
        var path = places.closeAndGetRenameFolderPlace();
        var nameOfFolder = name.getText();
        var pathRename = path.getParent().resolve(nameOfFolder);
        Files.move(path, pathRename);
    }
}
