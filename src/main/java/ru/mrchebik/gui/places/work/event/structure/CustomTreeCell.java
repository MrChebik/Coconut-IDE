package ru.mrchebik.gui.places.work.event.structure;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeCell;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.mrchebik.gui.places.creator.object.ObjectPlace;
import ru.mrchebik.gui.places.work.event.structure.event.PasteEvent;
import ru.mrchebik.model.CommandPath;
import ru.mrchebik.model.Project;

import java.nio.file.Path;

/**
 * Created by mrchebik on 9/15/17.
 */
@RequiredArgsConstructor
public class CustomTreeCell extends TreeCell<Path> {
    @NonNull private Project project;
    @NonNull private ObjectPlace objectPlace;
    @NonNull private CommandPath commandPath;

    @Override
    public void updateItem(Path path, boolean empty) {
        super.updateItem(path, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
            setContextMenu(null);
        } else {
            boolean isDirectory = path.toFile().isDirectory();

            setText(path.getFileName().toString());
            setGraphic(getTreeItem().getGraphic());

            MenuItem createFile = new MenuItem("Create File");
            MenuItem createFolder = new MenuItem("Create Folder");
            MenuItem cut = new MenuItem("Cut");
            MenuItem copy = new MenuItem("Copy");
            MenuItem paste = new MenuItem("Paste");
            MenuItem rename = new MenuItem("Rename");
            MenuItem delete = new MenuItem("Delete");

            if (!isDirectory) {
                createFile.setDisable(true);
                createFolder.setDisable(true);
                paste.setDisable(true);
            }
            if (path.equals(project.getPath())) {
                cut.setDisable(true);
                copy.setDisable(true);
                paste.setDisable(true);
                delete.setDisable(true);
            }

            createFile.setOnAction(event -> {
                objectPlace.setType("Create File");
                objectPlace.setPath(path);
                objectPlace.start();
            });

            createFolder.setOnAction(event -> {
                objectPlace.setType("Create Folder");
                objectPlace.setPath(path);
                objectPlace.start();
            });

            cut.setOnAction(event -> {
                commandPath.setCommand("Cut");
                commandPath.setPath(path);
            });

            copy.setOnAction(event -> {
                commandPath.setCommand("Copy");
                commandPath.setPath(path);
            });

            PasteEvent pasteEvent = new PasteEvent(commandPath, path);
            paste.setOnAction(pasteEvent);

            rename.setOnAction(event -> {
                objectPlace.setType("Rename " + (isDirectory ? "Folder" : "File"));
                objectPlace.setPath(path);
                objectPlace.start();
            });

            delete.setOnAction(event -> project.deleteFile(path));

            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems()
                    .addAll(createFile, createFolder, new SeparatorMenuItem(), cut, copy, paste, new SeparatorMenuItem(), rename, new SeparatorMenuItem(), delete);
            setContextMenu(contextMenu);
        }
    }
}
