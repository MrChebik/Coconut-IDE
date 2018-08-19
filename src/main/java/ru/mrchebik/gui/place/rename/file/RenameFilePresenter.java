package ru.mrchebik.gui.place.rename.file;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import lombok.SneakyThrows;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.injection.CollectorComponents;

import java.io.IOException;
import java.nio.file.Files;

public class RenameFilePresenter extends KeyHelper {
    @FXML
    private TextField name;

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
        var path = CollectorComponents.renameFilePlace.closeAndGetPath();
        var nameOfFile = name.getText();
        var pathRename = path.getParent().resolve(nameOfFile);
        Files.move(path, pathRename);
    }
}
