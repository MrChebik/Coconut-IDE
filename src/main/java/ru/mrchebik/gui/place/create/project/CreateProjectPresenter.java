package ru.mrchebik.gui.place.create.project;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import lombok.SneakyThrows;
import ru.mrchebik.gui.place.start.StartPlace;
import ru.mrchebik.gui.place.work.WorkPlace;
import ru.mrchebik.project.Projects;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import static ru.mrchebik.gui.key.KeyHelper.isEnter;

public class CreateProjectPresenter implements Initializable {
    @FXML
    private TextField projectName;
    @FXML
    private TextField projectPath;
    @Inject
    private CreateProjectPlace createProjectPlace;
    @Inject
    private StartPlace startPlace;

    private boolean wasChanged;

    @FXML
    private void handleCreateProject() {
        newProject();
    }

    @FXML
    private void handleCreateProjectWithKeyOnName(KeyEvent event) {
        if (isEnter(event))
            newProject();
    }

    @FXML
    private void handleCreateProjectWithKeyOnPath(KeyEvent event) {
        if (isEnter(event))
            newProject();
    }

    @FXML
    private void handleEditPath() {
        var target = computeFile();

        var directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(target);
        directoryChooser.setTitle("Choose Folder");

        var file = directoryChooser.showDialog(createProjectPlace.getStage());
        if (file != null) {
            projectName.setText(file.getName());
            projectPath.setText(file.getPath());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCorePath();
        projectPath.setText(Projects.pathString);
        projectPath.textProperty().addListener(this::computePropertyProjectPath);
        projectName.textProperty().addListener(this::computePropertyProjectName);
    }

    private void closeOtherWindows() {
        createProjectPlace.close();
        startPlace.close();
    }

    private File computeFile() {
        var path = Paths.get(projectPath.getText());
        var pathTarget = Files.exists(path) ?
                    path
                :
                    Paths.get(System.getProperty("user.home"));

        return pathTarget.toFile();
    }

    private void computePropertyProjectName(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!wasChanged &&
                pathStartsWithPathOfProjects()) {
            projectPath.setText(Projects.pathString + newValue);
            wasChanged = false;
        } else
            wasChanged = true;
    }

    private void computePropertyProjectPath(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        projectPath.setText(newValue);
        wasChanged = true;
    }

    @SneakyThrows(IOException.class)
    private void initializeCorePath() {
        var path = Projects.path;
        if (!Files.exists(path))
            Files.createDirectory(path);
    }

    private void newProject() {
        //PropertyCollector.create().writeProject(projectName.getText(), projectPath.getText());

        startWorkPlace();
        closeOtherWindows();
    }

    private boolean pathStartsWithPathOfProjects() {
        return projectPath.getText().startsWith(Projects.pathString);
    }

    private void startWorkPlace() {
        var name = projectName.getText();
        var path = Paths.get(projectPath.getText());

        var workPlace = new WorkPlace();
        workPlace.start(name, path);
    }
}
