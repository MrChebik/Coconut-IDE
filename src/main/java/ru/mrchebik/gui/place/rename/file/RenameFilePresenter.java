package ru.mrchebik.gui.place.rename.file;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lombok.SneakyThrows;
import ru.mrchebik.model.ActionPlaces;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;

import static ru.mrchebik.gui.key.KeyHelper.isEnter;

public class RenameFilePresenter {
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
        var path = places.closeAndGetRenameFilePlace();
        var nameOfFile = name.getText();
        var pathRename = path.getParent().resolve(nameOfFile);
        Files.move(path, pathRename);
    }
}
