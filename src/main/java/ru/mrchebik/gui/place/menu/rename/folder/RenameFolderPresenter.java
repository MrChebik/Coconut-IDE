package ru.mrchebik.gui.place.menu.rename.folder;

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

public class RenameFolderPresenter extends MenuPresenterAction implements Initializable {
    @FXML
    public Label nameL;
    @FXML
    public Button button;
    @FXML
    private TextField name;

    @FXML
    private void handleRename() {
        renameFile(name, CellPlaceConfig.closeAndGetPath(ViewHelper.RENAME_FOLDER));
    }

    @FXML
    private void handleRenameWithKey(KeyEvent event) {
        if (isEnter(event))
            renameFile(name, CellPlaceConfig.closeAndGetPath(ViewHelper.RENAME_FOLDER));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLocale(nameL, button, true);
    }
}
