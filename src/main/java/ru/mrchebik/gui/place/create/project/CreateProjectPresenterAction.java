package ru.mrchebik.gui.place.create.project;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import lombok.SneakyThrows;
import ru.mrchebik.gui.place.PlaceConfig;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.project.Project;
import ru.mrchebik.settings.PropertyCollector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class CreateProjectPresenterAction {
    private static boolean wasChanged;
    private static String initial;

    static void initLocale(Button button,
                           Button edit) {
        button.setText(Locale.getProperty("create_button", true));
        edit.setText(Locale.getProperty("edit_button", true));
    }

    static void callDirectoryChooser(TextField name, TextField path) {
        var target = computeFile(path);

        var directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(target);
        directoryChooser.setTitle(Locale.getProperty("choose_folder_title", true));

        var file = directoryChooser.showDialog(ViewHelper.CREATE_PROJECT.stage);
        if (file != null)
            setFields(file, name, path);
    }

    @SneakyThrows(IOException.class)
    static void initCorePath() {
        var path = Paths.get(PropertyCollector.projects);
        if (!Files.exists(path)) Files.createDirectory(path);
    }

    static void initListeners(TextField name,
                              TextField path) {
        name.textProperty().addListener((obs, oldV, newV) -> computePropertyProjectName(newV, path));
        path.textProperty().addListener((obs, oldV, newV) -> computePropertyProjectPath(newV, path));
    }

    static void initProjectPath(TextField field) {
        Path property = Paths.get(PropertyCollector.projects);
        Path userHome = Paths.get(System.getProperty("user.home"));
        field.setText(property.startsWith(userHome) ?
                property.equals(userHome) ?
                        "~/"
                        :
                        "~/" + userHome.relativize(property).toString() + "/"
                :
                PropertyCollector.projects);
        initial = field.getText();
    }

    static void newProject(TextField name,
                           TextField path) {
        Project.isOpen = false;
        initWorkPlace(name, path);
        PlaceConfig.closeWindow(ViewHelper.START, ViewHelper.CREATE_PROJECT);
    }

    private static void computePropertyProjectName(String newValue,
                                                   TextField path) {
        if (!wasChanged &&
                path.getText().startsWith(initial)) {
            path.setText(initial + newValue);
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

    private static void computePropertyProjectPath(String newValue,
                                                   TextField path) {
        path.setText(newValue);
        wasChanged = true;
    }

    private static void initWorkPlace(TextField nameField,
                                      TextField pathField) {
        var name = nameField.getText();
        var pathStr = pathField.getText();
        var path = Paths.get(pathStr.startsWith("~") ?
                System.getProperty("user.home") + pathStr.substring(1)
                :
                pathStr);

        Project.initializeProject(name, path);
        ViewHelper.WORK.start();
    }

    private static void setFields(File file, TextField name, TextField path) {
        name.setText(file.getName());
        path.setText(file.getPath());
    }
}
