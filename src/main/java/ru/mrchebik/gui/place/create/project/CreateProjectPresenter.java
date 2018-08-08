package ru.mrchebik.gui.place.create.project;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import lombok.SneakyThrows;
import ru.mrchebik.gui.place.PresenterHelper;
import ru.mrchebik.gui.place.start.StartPlace;
import ru.mrchebik.gui.place.work.WorkPlace;
import ru.mrchebik.project.Projects;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class CreateProjectPresenter extends PresenterHelper implements Initializable {
    @FXML
    private TextField projectName;
    @FXML
    private TextField projectPath;
    @Inject
    private CreateProjectPlace createProjectPlace;
    @Inject
    private Projects projects;
    @Inject
    private StartPlace startPlace;

    private boolean wasChanged;

    @FXML
    private void handleCreateProject() {
        newProject();
    }

    @FXML
    private void handleCreateProjectWithKeyOnName(KeyEvent event) {
        if (super.isEnter(event)) {
            newProject();
        }
    }

    @FXML
    private void handleCreateProjectWithKeyOnPath(KeyEvent event) {
        if (super.isEnter(event)) {
            newProject();
        }
    }

    @FXML
    private void handleEditPath() {
        File target = computeFile();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(target);
        directoryChooser.setTitle("Choose Folder");

        File file = directoryChooser.showDialog(createProjectPlace.getStage());
        if (file != null) {
            projectName.setText(file.getName());
            projectPath.setText(file.getPath());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCorePath();
        projectPath.setText(projects.getCorePathString());
        projectPath.textProperty().addListener(this::computePropertyProjectPath);
        projectName.textProperty().addListener(this::computePropertyProjectName);
    }

    private void closeOtherWindows() {
        createProjectPlace.close();
        startPlace.close();
    }

    private File computeFile() {
        Path path = Paths.get(projectPath.getText());
        Path pathTarget = Files.exists(path) ? path : Paths.get(System.getProperty("user.home"));

        return pathTarget.toFile();
    }

    private void computePropertyProjectName(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!wasChanged && pathStartsWithPathOfProjects()) {
            projectPath.setText(projects.getCorePathString() + newValue);
            wasChanged = false;
        } else {
            wasChanged = true;
        }
    }

    private void computePropertyProjectPath(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        projectPath.setText(newValue);
        wasChanged = true;
    }

    @SneakyThrows(IOException.class)
    private void initializeCorePath() {
        Path path = projects.getCorePath();
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }

    private void newProject() {
        //PropertyCollector.create().writeProject(projectName.getText(), projectPath.getText());

        startWorkPlace();
        closeOtherWindows();
    }

    private boolean pathStartsWithPathOfProjects() {
        return projectPath.getText().startsWith(projects.getCorePathString());
    }

    @SneakyThrows(IOException.class)
    private void startWorkPlace() {
        String name = projectName.getText();
        Path path = Paths.get(projectPath.getText());

        WorkPlace workPlace = new WorkPlace();
        workPlace.start(name, path);
    }
}
