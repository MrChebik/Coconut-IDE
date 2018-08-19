package ru.mrchebik.gui.place.create.project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.gui.place.start.StartPlace;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateProjectPresenter extends KeyHelper implements Initializable {
    @FXML
    private TextField projectName;
    @FXML
    private TextField projectPath;
    @Inject
    private CreateProjectPlace createProjectPlace;
    @Inject
    private StartPlace startPlace;

    @FXML
    private void handleCreateProject() {
        CreateProjectPresenterHelper.newProject(projectName, projectPath, startPlace, createProjectPlace);
    }

    @FXML
    private void handleCreateProjectWithKeyOnName(KeyEvent event) {
        if (isEnter(event))
            CreateProjectPresenterHelper.newProject(projectName, projectPath, startPlace, createProjectPlace);
    }

    @FXML
    private void handleCreateProjectWithKeyOnPath(KeyEvent event) {
        if (isEnter(event))
            CreateProjectPresenterHelper.newProject(projectName, projectPath, startPlace, createProjectPlace);
    }

    @FXML
    private void handleEditPath() {
        CreateProjectPresenterHelper.callDirectoryChooser(projectName, projectPath, createProjectPlace);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateProjectPresenterHelper.initCorePath();
        CreateProjectPresenterHelper.initProjectPath(projectPath);
        CreateProjectPresenterHelper.initListeners(projectName, projectPath);
    }
}
