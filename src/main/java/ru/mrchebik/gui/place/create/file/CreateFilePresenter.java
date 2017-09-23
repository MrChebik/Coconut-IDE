package ru.mrchebik.gui.place.create.file;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.gui.place.PresenterHelper;
import ru.mrchebik.model.ActionPlaces;
import ru.mrchebik.model.Project;

import javax.inject.Inject;
import java.nio.file.Path;

public class CreateFilePresenter extends PresenterHelper {
    @FXML
    private TextField name;
    @Inject
    private ActionPlaces places;
    @Inject
    private Project project;

    @FXML
    private void handleCreateFile() {
        createFile();
    }

    @FXML
    private void handleCreateFileWithKey(KeyEvent event) {
        if (super.isEnter(event)) {
            createFile();
        }
    }

    private void createFile() {
        Path path = places.getPathOfCreateFilePlace().resolve(name.getText());
        project.createFile(path);

        places.closeCreateFilePlace();
    }
}
