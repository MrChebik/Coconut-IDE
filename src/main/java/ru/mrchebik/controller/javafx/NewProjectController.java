package ru.mrchebik.controller.javafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import ru.mrchebik.controller.actions.newProject.NewProject;
import ru.mrchebik.model.Projects;
import ru.mrchebik.view.StartOfWorking;
import ru.mrchebik.view.WorkStation;

import java.io.File;
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

    private void newProject() {
        NewProject newProject = new NewProject(projectName.getText(), projectPath.getText());
        newProject.start();

        ru.mrchebik.view.NewProject.close();
        StartOfWorking.close();

        try {
            WorkStation.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
