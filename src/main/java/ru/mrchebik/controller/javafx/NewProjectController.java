package ru.mrchebik.controller.javafx;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

    @FXML private void handleCreateProject() throws Exception {
        Project.setName(projectName.getText());
        Project.setPath(projectPath.getText());

        NewProjectAction.start();

        NewProject.close();
        StartOfWorking.close();

        WorkStation.start(StartOfWorking.getStage());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Projects.setPath("/home/" + System.getProperty("user.name") + "/CoconutProjects/");
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
