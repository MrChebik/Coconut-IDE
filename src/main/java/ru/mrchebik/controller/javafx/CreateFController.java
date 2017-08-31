package ru.mrchebik.controller.javafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.view.CreateF;
import ru.mrchebik.view.WorkStation;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by mrchebik on 8/30/17.
 */
public class CreateFController implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private Button create;

    private void create() throws IOException {
        CreateF.close();

        WorkStationController controller = WorkStation.getFxmlLoader().getController();

        if ("Create File".equals(CreateF.getType())) {
            new File(CreateF.getPath().toUri().getPath() + File.separator + name.getText()).createNewFile();
        } else if ("Create Folder".equals(CreateF.getType())) {
            new File(CreateF.getPath().toUri().getPath() + File.separator + name.getText()).mkdir();
        } else {
            File file = new File(CreateF.getPath().toUri());
            File newFile = new File(file.getPath().substring(0, file.getPath().length() - file.getName().length()) + name.getText());
            boolean isRenamed = file.renameTo(newFile);

            if (newFile.isDirectory() && isRenamed) {
                controller.setTargetToRename(CreateF.getPath());
                controller.setRenamedFile(newFile.toPath());
            }
        }

        controller.loadTree();
    }

    @FXML
    private void handleCreateFWithKey(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            create();
        }
    }

    @FXML
    private void handleCreateF() throws IOException {
        create();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        create.setText(CreateF.getType().substring(0, 6));
    }
}
