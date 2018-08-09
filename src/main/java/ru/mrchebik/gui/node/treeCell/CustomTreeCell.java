package ru.mrchebik.gui.node.treeCell;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeCell;
import lombok.AllArgsConstructor;
import ru.mrchebik.gui.node.treeCell.event.PasteEvent;
import ru.mrchebik.helper.FileHelper;
import ru.mrchebik.model.ActionPlaces;
import ru.mrchebik.model.CommandPath;
import ru.mrchebik.project.Project;

import java.nio.file.Files;
import java.nio.file.Path;

@AllArgsConstructor
public class CustomTreeCell extends TreeCell<Path> {
    private CommandPath commandPath;
    private ActionPlaces places;

    @Override
    public void updateItem(Path path, boolean empty) {
        super.updateItem(path, empty);

        if (empty) {
            makeEmpty(this);
        } else {
            setText(path.getFileName().toString());
            setGraphic(getTreeItem().getGraphic());
            setContextMenu(addContext(path));
        }
    }

    private ContextMenu addContext(Path path) {
        MenuItem createFile = new MenuItem("Create File");
        MenuItem createFolder = new MenuItem("Create Folder");
        MenuItem copy = new MenuItem("Copy");
        MenuItem cut = new MenuItem("Cut");
        MenuItem paste = new MenuItem("Paste");
        MenuItem rename = new MenuItem("Rename");
        MenuItem delete = new MenuItem("Delete");

        if (!Files.isDirectory(path)) {
            createFile.setDisable(true);
            createFolder.setDisable(true);
            paste.setDisable(true);
        } else if (path.equals(Project.path)) {
            cut.setDisable(true);
            copy.setDisable(true);
            delete.setDisable(true);
        }

        createFile.setOnAction(event -> places.runCreateFilePlace(path));

        createFolder.setOnAction(event -> places.runCreateFolderPlace(path));

        copy.setOnAction(event -> {
            commandPath.setCommand("Copy");
            commandPath.setPath(path);
        });

        cut.setOnAction(event -> {
            commandPath.setCommand("Cut");
            commandPath.setPath(path);
        });

        PasteEvent pasteEvent = new PasteEvent(commandPath, path);
        paste.setOnAction(pasteEvent);

        rename.setOnAction(event -> {
            if (Files.isDirectory(path)) {
                places.runRenameFilePlace(path);
            } else {
                places.runRenameFolderPlace(path);
            }
        });

        delete.setOnAction(event -> FileHelper.deleteFile(path));

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems()
                .addAll(createFile, createFolder,
                        new SeparatorMenuItem(),
                        copy, cut, paste,
                        new SeparatorMenuItem(),
                        rename,
                        new SeparatorMenuItem(),
                        delete);
        return contextMenu;
    }

    private void makeEmpty(CustomTreeCell cell) {
        cell.setText(null);
        cell.setGraphic(null);
        cell.setContextMenu(null);
        cell.setStyle(null);
    }
}
