package ru.mrchebik.gui.places.creator.project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import lombok.SneakyThrows;
import ru.mrchebik.gui.places.start.StartPlace;
import ru.mrchebik.gui.places.work.WorkPlace;
import ru.mrchebik.model.Projects;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Created by mrchebik on 8/29/17.
 */
public class ProjectPresenter implements Initializable {
    @FXML private TextField projectName;
    @FXML private TextField projectPath;

    private boolean wasChanged;

    @Inject private StartPlace startPlace;
    @Inject private ProjectPlace projectPlace;
    @Inject private Projects projects;

    @SneakyThrows(IOException.class)
    private void newProject() {
        projectPlace.close();
        startPlace.close();

        String name = projectName.getText();
        Path path = Paths.get(projectPath.getText());

        WorkPlace workPlace = new WorkPlace();
        workPlace.start(name, path);
    }

    @FXML private void handleEditPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        String pathProject = projectPath.getText();

        String targetPath = Files.exists(Paths.get(pathProject)) ? pathProject : System.getProperty("user.home");
        File target = new File(targetPath);

        directoryChooser.setInitialDirectory(target);
        directoryChooser.setTitle("Choose Folder");

        File file = directoryChooser.showDialog(projectPlace.getStage());

        if (file != null) {
            projectName.setText(file.getName());
            projectPath.setText(file.getPath());
        }
    }

    @FXML
    private void handleCreateProject() {
        newProject();
    }

    @FXML
    private void handleCreateProjectWithKeyOnName(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            newProject();
    }

    @FXML
    private void handleCreateProjectWithKeyOnPath(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            newProject();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeCorePath();

        String pathStringOfProjects = projects.getPath().toString() + File.separator;

        projectPath.setText(pathStringOfProjects);
        projectPath.textProperty().addListener((observable, oldValue, newValue) -> {
            wasChanged = true;
            projectPath.setText(newValue);
        });
        projectName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!wasChanged && projectPath.getText().startsWith(pathStringOfProjects)) {
                projectPath.setText(pathStringOfProjects + newValue);
                wasChanged = false;
            } else
                wasChanged = true;
        });
    }

    @SneakyThrows(IOException.class)
    private void initializeCorePath() {
        Path path = projects.getPath();
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }
}
