package ru.mrchebik.gui.place.create.project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.gui.place.start.StartPresenter;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateProjectPresenter extends KeyHelper implements Initializable {
    @FXML
    public TextField projectName, projectPath;
    @FXML
    private Button button, edit;
    @FXML
    public AnchorPane mask;

    public boolean isClickedOfNonMask;

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

    @FXML
    private void maskClicked() {
        if (!isClickedOfNonMask) {
            ((AnchorPane) ViewHelper.START.view.getView()).getChildren().remove(1);
            StartPresenter presenter = ((StartPresenter) ViewHelper.START.view.getPresenter());
            presenter.createProject.setFocusTraversable(true);
            presenter.openProject.setFocusTraversable(true);
            presenter.setupHomeButton.setFocusTraversable(true);
            ((StartPresenter) ViewHelper.START.view.getPresenter()).titleZone.setEffect(null);
            presenter.createProject.requestFocus();
        } else
            isClickedOfNonMask = false;
    }

    @FXML
    private void nonMaskClicked() {
        isClickedOfNonMask = true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CreateProjectPresenterAction.initLocale(button, edit);
        CreateProjectPresenterAction.initCorePath();
        CreateProjectPresenterAction.initProjectPath(projectPath);
        CreateProjectPresenterAction.initListeners(projectName, projectPath);
    }
}
