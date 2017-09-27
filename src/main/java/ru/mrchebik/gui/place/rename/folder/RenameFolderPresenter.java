package ru.mrchebik.gui.place.rename.folder;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lombok.SneakyThrows;
import ru.mrchebik.gui.place.PresenterHelper;
import ru.mrchebik.model.ActionPlaces;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RenameFolderPresenter extends PresenterHelper {
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
        if (super.isEnter(event)) {
            renameFile();
        }
    }

    @SneakyThrows(IOException.class)
    private void renameFile() {
        Path path = places.closeAndGetRenameFolderPlace();
        String nameOfFolder = name.getText();
        Path pathRename = path.getParent().resolve(nameOfFolder);
        Files.move(path, pathRename);
    }
}