package ru.mrchebik.gui.place.create.project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.gui.place.start.StartPlace;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateProjectPresenter extends KeyHelper implements Initializable {
    @FXML
    private TextField projectName, projectPath;
    @FXML
    private Button button, edit;
    @FXML
    private Label name, path;
    @Inject
    private CreateProjectPlace createProjectPlace;
    @Inject
    private StartPlace startPlace;

    @FXML
    private void handleCreateProject() {
        CreateProjectPresenterAction.newProject(projectName, projectPath);
    }

    @FXML
    private void handleCreateProjectWithKeyOnName(KeyEvent event) {
        if (isEnter(event))
            CreateProjectPresenterAction.newProject(projectName, projectPath);
    }

    @FXML
    private void handleCreateProjectWithKeyOnPath(KeyEvent event) {
        if (isEnter(event))
            CreateProjectPresenterAction.newProject(projectName, projectPath);
    }

    @FXML
    private void handleEditPath() {
        CreateProjectPresenterAction.callDirectoryChooser(projectName, projectPath);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateProjectPresenterAction.initLocale(button, edit, name, path);
        CreateProjectPresenterAction.initCorePath();
        CreateProjectPresenterAction.initProjectPath(projectPath);
        CreateProjectPresenterAction.initListeners(projectName, projectPath);
    }
}
