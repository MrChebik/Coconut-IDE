package ru.mrchebik.gui.place.create.file;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.helper.FileHelper;
import ru.mrchebik.model.ActionPlaces;

import javax.inject.Inject;

import static ru.mrchebik.gui.key.KeyHelper.isEnter;

public class CreateFilePresenter {
    @FXML
    private TextField name;
    @Inject
    private ActionPlaces places;

    @FXML
    private void handleCreateFile() {
        FileHelper.createFilePresenter(places, name);
    }

    @FXML
    private void handleCreateFileWithKey(KeyEvent event) {
        if (isEnter(event))
            FileHelper.createFilePresenter(places, name);
    }
}
