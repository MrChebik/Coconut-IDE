package ru.mrchebik.gui.place.menu.create.folder;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.gui.place.CellPlaceConfig;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.gui.place.menu.MenuPresenterAction;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateFolderPresenter extends MenuPresenterAction implements Initializable {
    @FXML
    public Label nameL;
    @FXML
    public Button button;
    @FXML
    private TextField name;

    @FXML
    private void handleCreateFolder() {
        createFile(name, CellPlaceConfig.closeAndGetPath(ViewHelper.CREATE_FOLDER), false);
    }

    @FXML
    private void handleCreateFolderWithKey(KeyEvent event) {
        if (isEnter(event))
            createFile(name, CellPlaceConfig.closeAndGetPath(ViewHelper.CREATE_FOLDER), false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLocale(nameL, button, true);
    }
}
