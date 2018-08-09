package ru.mrchebik.gui.place.create.folder;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.helper.FileHelper;
import ru.mrchebik.model.ActionPlaces;

import javax.inject.Inject;

import static ru.mrchebik.gui.key.KeyHelper.isEnter;

public class CreateFolderPresenter {
    @FXML
    private TextField name;
    @Inject
    private ActionPlaces places;

    @FXML
    private void handleCreateFolder() {
        createFolder();
    }

    @FXML
    private void handleCreateFolderWithKey(KeyEvent event) {
        if (isEnter(event))
            createFolder();
    }

    private void createFolder() {
        var pathFromPlace = places.closeAndGetCreateFolderPlace();
        var nameOfFolder = name.getText();
        var path = pathFromPlace.resolve(nameOfFolder);
        FileHelper.createFolder(path);
    }
}
