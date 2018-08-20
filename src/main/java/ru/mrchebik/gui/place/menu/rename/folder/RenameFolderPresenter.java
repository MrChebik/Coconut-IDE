package ru.mrchebik.gui.place.menu.rename.folder;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.gui.place.menu.MenuPresenterHelper;
import ru.mrchebik.injection.ComponentsCollector;

import java.net.URL;
import java.util.ResourceBundle;

public class RenameFolderPresenter extends MenuPresenterHelper implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private Label nameL;
    @FXML
    private Button button;

    @FXML
    private void handleRename() {
        renameFile(name, ComponentsCollector.renameFolderPlace.closeAndGetPath());
    }

    @FXML
    private void handleRenameWithKey(KeyEvent event) {
        if (isEnter(event))
            renameFile(name, ComponentsCollector.renameFolderPlace.closeAndGetPath());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLocale(nameL, button, true);
    }
}
