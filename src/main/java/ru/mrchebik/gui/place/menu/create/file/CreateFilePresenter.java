package ru.mrchebik.gui.place.menu.create.file;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.gui.collector.ComponentsCollector;
import ru.mrchebik.gui.place.menu.MenuPresenterHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateFilePresenter extends MenuPresenterHelper implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private Label nameL;
    @FXML
    private Button button;

    @FXML
    private void handleCreateFile() {
        createFile(name, ComponentsCollector.createFilePlace.closeAndGetPath(), true);
    }

    @FXML
    private void handleCreateFileWithKey(KeyEvent event) {
        if (isEnter(event))
            createFile(name, ComponentsCollector.createFilePlace.closeAndGetPath(), true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLocale(nameL, button, false);
    }
}
