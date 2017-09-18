package ru.mrchebik.gui.places.creator.object;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.SneakyThrows;
import ru.mrchebik.model.Project;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

/**
 * Created by mrchebik on 8/30/17.
 */

public class ObjectPresenter implements Initializable {
    @FXML private TextField name;
    @FXML private Button create;

    @Inject private Project project;
    @Inject private ObjectPlace objectPlace;

    @FXML
    private void handleCreateObject() {
        create();
    }

    @FXML
    private void handleCreateObjectWithKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            create();
    }

    @SneakyThrows(IOException.class)
    private void create() {
        objectPlace.close();

        Path path = objectPlace.getPath();
        String type = objectPlace.getType();

        String newName = name.getText();
        Path pathAdd = path.resolve(newName);
        Path pathRename = path.getParent().resolve(newName);
        /*File file;*/
        if ("Create File".equals(type))
            project.createFile(pathAdd);
        else if ("Create Folder".equals(type))
            project.createFolder(pathAdd);
        else
            Files.move(path, pathRename);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String createType = objectPlace.getType().substring(0, 6);
        create.setText(createType);
    }
}
