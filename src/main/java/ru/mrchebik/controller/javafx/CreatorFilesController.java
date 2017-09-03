package ru.mrchebik.controller.javafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.controller.actions.newProject.paths.CreateProjectPaths;
import ru.mrchebik.view.CreatorFiles;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by mrchebik on 8/30/17.
 */
public class CreatorFilesController implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private Button create;

    private void create() throws IOException {
        CreatorFiles.close();

        String pathOfCreator = CreatorFiles.getPath().toString();
        String path = pathOfCreator + File.separator + name.getText();
        File file;
        if ("Create File".equals(CreatorFiles.getType())) {
            CreateProjectPaths.createFileToProject(path);
        } else if ("Create Folder".equals(CreatorFiles.getType())) {
            CreateProjectPaths.createFolderToProject(path);
        } else {
            file = new File(pathOfCreator);
            String nameFromPath = file.getName();
            int indexWithoutName = pathOfCreator.length() - nameFromPath.length();

            String newPath = pathOfCreator.substring(0, indexWithoutName) + name.getText();
            File newFile = new File(newPath);

            file.renameTo(newFile);
        }
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
        create.setText(CreatorFiles.getType().substring(0, 6));
    }
}
