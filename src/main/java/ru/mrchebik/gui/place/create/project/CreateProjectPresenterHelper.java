package ru.mrchebik.gui.place.create.project;

import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import lombok.SneakyThrows;
import ru.mrchebik.gui.place.start.StartPlace;
import ru.mrchebik.gui.place.work.WorkPlace;
import ru.mrchebik.project.Projects;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class CreateProjectPresenterHelper {
    private static boolean wasChanged;

    static void callDirectoryChooser(TextField name, TextField path, CreateProjectPlace createProjectPlace) {
        var target = computeFile(path);

        var directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(target);
        directoryChooser.setTitle("Choose Folder");

        var file = directoryChooser.showDialog(createProjectPlace.getStage());
        if (file != null)
            setFields(file, name, path);
    }

    @SneakyThrows(IOException.class)
    static void initCorePath() {
        var path = Projects.path;
        if (!Files.exists(path))
            Files.createDirectory(path);
    }

    static void initListeners(TextField name,
                              TextField path) {
        name.textProperty().addListener((obs, oldV, newV) -> computePropertyProjectName(newV, path));
        path.textProperty().addListener((obs, oldV, newV) -> computePropertyProjectPath(newV, path));
    }

    static void initProjectPath(TextField field) {
        field.setText(Projects.pathString);
    }

    static void newProject(TextField name,
                           TextField path,
                           StartPlace startPlace,
                           CreateProjectPlace createProjectPlace) {
        //PropertyCollector.writeProject(projectName.getText(), projectPath.getText());

        initWorkPlace(name, path);
        closeWindows(startPlace, createProjectPlace);
    }

    private static void closeWindows(StartPlace         startPlace,
                                     CreateProjectPlace createProjectPlace) {
        startPlace.close();
        createProjectPlace.close();
    }

    private static void computePropertyProjectName(String    newValue,
                                                   TextField path) {
        if (!wasChanged &&
                path.getText().startsWith(Projects.pathString)) {
            path.setText(Projects.pathString + newValue);
            wasChanged = false;
        } else
            wasChanged = true;
    }

    private static File computeFile(TextField pathField) {
        var path = Paths.get(pathField.getText());
        var pathTarget = Files.exists(path) ?
                path
                :
                Paths.get(System.getProperty("user.home"));

        return pathTarget.toFile();
    }

    private static void computePropertyProjectPath(String    newValue,
                                                   TextField path) {
        path.setText(newValue);
        wasChanged = true;
    }

    private static void initWorkPlace(TextField nameField,
                                      TextField pathField) {
        var name = nameField.getText();
        var path = Paths.get(pathField.getText());

        var workPlace = new WorkPlace();
        workPlace.start(name, path);
    }

    private static void setFields(File file, TextField name, TextField path) {
        name.setText(file.getName());
        path.setText(file.getPath());
    }
}
