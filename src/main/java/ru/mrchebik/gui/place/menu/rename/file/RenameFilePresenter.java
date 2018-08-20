package ru.mrchebik.gui.place.menu.rename.file;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.gui.collector.ComponentsCollector;
import ru.mrchebik.gui.place.menu.MenuPresenterAction;

import java.net.URL;
import java.util.ResourceBundle;

public class RenameFilePresenter extends MenuPresenterAction implements Initializable {
    @FXML
    private TextField name;
    @FXML
    public Label nameL;
    @FXML
    public Button button;

    @FXML
    private void handleRename() {
        renameFile(name, ComponentsCollector.renameFilePlace.closeAndGetPath());
    }

    @FXML
    private void handleRenameWithKey(KeyEvent event) {
        if (isEnter(event))
            renameFile(name, ComponentsCollector.renameFilePlace.closeAndGetPath());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLocale(nameL, button, true);
    }
}