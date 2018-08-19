package ru.mrchebik.gui.place.create.folder;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.helper.FileHelper;
import ru.mrchebik.injection.CollectorComponents;

public class CreateFolderPresenter extends KeyHelper {
    @FXML
    private TextField name;

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
        var pathFromPlace = CollectorComponents.createFolderPlace.closeAndGetPath();
        var nameOfFolder = name.getText();
        var path = pathFromPlace.resolve(nameOfFolder);
        FileHelper.createFolder(path);
    }
}
