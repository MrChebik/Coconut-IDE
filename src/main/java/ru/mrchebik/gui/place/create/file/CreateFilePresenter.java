package ru.mrchebik.gui.place.create.file;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.helper.FileHelper;

public class CreateFilePresenter extends KeyHelper {
    @FXML
    private TextField name;

    @FXML
    private void handleCreateFile() {
        FileHelper.createFilePresenter(name);
    }

    @FXML
    private void handleCreateFileWithKey(KeyEvent event) {
        if (isEnter(event))
            FileHelper.createFilePresenter(name);
    }
}
