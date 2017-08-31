package ru.mrchebik.controller.javafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import ru.mrchebik.controller.NewProjectAction;
import ru.mrchebik.model.Project;
import ru.mrchebik.model.Projects;
import ru.mrchebik.view.NewProject;
import ru.mrchebik.view.StartOfWorking;
import ru.mrchebik.view.WorkStation;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by mrchebik on 8/29/17.
 */
public class NewProjectController implements Initializable {
    @FXML private TextField projectName;
    @FXML private TextField projectPath;

    private boolean wasChanged;

    private void newProject() throws Exception {
        Project.setName(projectName.getText());
        Project.setPath(projectPath.getText());

        new NewProjectAction().start();

        NewProject.close();
        StartOfWorking.close();

        WorkStation.start();
    }

    @FXML private void handleEditPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setInitialDirectory(new File(projectPath.getText().startsWith("/") ? new File(projectPath.getText()).exists() ? projectPath.getText() : System.getProperty("user.home") : System.getProperty("user.home")));
        directoryChooser.setTitle("Choose Folder");

        File file = directoryChooser.showDialog(NewProject.getStage());

        if (file != null) {
            projectName.setText(file.getName());
            projectPath.setText(file.getPath());
        }
    }

    @FXML private void handleCreateProject() throws Exception {
        newProject();
    }

    @FXML private void handleCreateProjectWithKeyOnName(KeyEvent event) throws Exception {
        if (event.getCode() == KeyCode.ENTER) {
            newProject();
        }
    }

    @FXML private void handleCreateProjectWithKeyOnPath(KeyEvent event) throws Exception {
        if (event.getCode() == KeyCode.ENTER) {
            newProject();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Projects.setPath(System.getProperty("user.home") + File.separator + "CoconutProjects" + File.separator);

        new File(Projects.getPath()).mkdirs();

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
