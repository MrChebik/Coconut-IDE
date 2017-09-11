package ru.mrchebik.controller.javafx;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import lombok.SneakyThrows;
import ru.mrchebik.model.Projects;
import ru.mrchebik.model.project.Project;
import ru.mrchebik.model.project.ProjectFactory;
import ru.mrchebik.view.StartOfWorking;
import ru.mrchebik.view.WorkStation;

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
public class NewProjectController implements Initializable {
    @FXML private TextField projectName;
    @FXML private TextField projectPath;

    private boolean wasChanged;

    @Inject
    private ProjectFactory projectFactory;
    @Inject
    private Project project;

    @SneakyThrows(IOException.class)
    private void newProject() {
        ru.mrchebik.view.NewProject.close();
        StartOfWorking.close();

        String name = projectName.getText();
        Path path = Paths.get(projectPath.getText());

        Path pathOfSource = Paths.get(path.toString(), "src");
        Path pathOfOut = Paths.get(path.toString(), "out");

        WorkStation.start();

        project = projectFactory.create(name, path, pathOfOut, pathOfSource);
        project.build();
    }

    @FXML private void handleEditPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        String pathProject = projectPath.getText();
        Path path = Paths.get(pathProject);

        String targetPath;

        boolean isStartOfSlash = pathProject.startsWith(File.separator);
        if (isStartOfSlash && Files.exists(path)) {
            targetPath = pathProject;
        } else {
            targetPath = System.getProperty("user.home");
        }

        File target = new File(targetPath);

        directoryChooser.setInitialDirectory(target);
        directoryChooser.setTitle("Choose Folder");

        File file = directoryChooser.showDialog(ru.mrchebik.view.NewProject.getStage());

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
        if (event.getCode() == KeyCode.ENTER) {
            newProject();
        }
    }

    @FXML
    private void handleCreateProjectWithKeyOnPath(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            newProject();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Projects.setPath(System.getProperty("user.home") + File.separator + "CoconutProjects" + File.separator);

        File file = new File(Projects.getPath());
        file.mkdirs();

        projectPath.setText(Projects.getPath());
        projectPath.textProperty().addListener((observable, oldValue, newValue) -> {
            wasChanged = true;
            projectPath.setText(newValue);
        });
        projectName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!wasChanged && projectPath.getText().startsWith(Projects.getPath())) {
                projectPath.setText(Projects.getPath() + newValue);
                wasChanged = false;
            } else {
                wasChanged = true;
            }
        });
    }
}
