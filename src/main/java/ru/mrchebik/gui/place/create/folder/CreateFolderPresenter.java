package ru.mrchebik.gui.place.create.folder;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.helper.FileHelper;
import ru.mrchebik.model.ActionPlaces;
import ru.mrchebik.project.Project;

import javax.inject.Inject;
import java.nio.file.Path;

import static ru.mrchebik.gui.key.KeyHelper.isEnter;

public class CreateFolderPresenter {
    @FXML
    private TextField name;
    @Inject
    private ActionPlaces places;
    @Inject
    private Project project;

    @FXML
    private void handleCreateFolder() {
        createFolder();
    }

    @FXML
    private void handleCreateFolderWithKey(KeyEvent event) {
        if (isEnter(event)) {
            createFolder();
        }
    }

    private void createFolder() {
        Path pathFromPlace = places.closeAndGetCreateFolderPlace();
        String nameOfFolder = name.getText();
        Path path = pathFromPlace.resolve(nameOfFolder);
        FileHelper.createFolder(path);
    }
}
