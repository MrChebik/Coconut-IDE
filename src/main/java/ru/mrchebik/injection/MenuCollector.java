package ru.mrchebik.injection;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.ContextMenuEvent;
import ru.mrchebik.gui.node.treeCell.event.PasteEvent;
import ru.mrchebik.helper.FileHelper;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.model.CommandPath;
import ru.mrchebik.project.Project;

import java.nio.file.Files;
import java.nio.file.Path;

public class MenuCollector {
    public static ContextMenu contextMenu;

    private static MenuItem createFile;
    private static MenuItem createFolder;
    private static MenuItem copy;
    private static MenuItem cut;
    private static MenuItem paste;
    private static MenuItem rename;
    private static MenuItem delete;

    private static CommandPath commandPath;

    static {
        initItems();
        addItems();
    }

    private static void initItems() {
        contextMenu = new ContextMenu();

        createFile = new MenuItem(Locale.CREATE_FILE_MENU);
        createFolder = new MenuItem(Locale.CREATE_FOLDER_MENU);
        copy = new MenuItem(Locale.COPY_MENU);
        cut = new MenuItem(Locale.CUT_MENU);
        paste = new MenuItem(Locale.PASTE_MENU);
        rename = new MenuItem(Locale.RENAME_BUTTON);
        delete = new MenuItem(Locale.DELETE_MENU);

        commandPath = CommandPath.create();
    }

    private static void addItems() {
        contextMenu.getItems()
                .addAll(createFile, createFolder,
                        new SeparatorMenuItem(),
                        copy, cut, paste,
                        new SeparatorMenuItem(),
                        rename,
                        new SeparatorMenuItem(),
                        delete);
    }

    private static void initListeners(Path path) {
        createFile.setOnAction(event -> ComponentsCollector.createFilePlace.runAndSetPath(path));

        createFolder.setOnAction(event -> ComponentsCollector.createFolderPlace.runAndSetPath(path));

        copy.setOnAction(event -> {
            commandPath.command = "Copy";
            commandPath.path = path;
        });

        cut.setOnAction(event -> {
            commandPath.command = "Cut";
            commandPath.path = path;
        });

        PasteEvent pasteEvent = new PasteEvent(commandPath, path);
        paste.setOnAction(pasteEvent);

        rename.setOnAction(event -> {
            if (!Files.isDirectory(path))
                ComponentsCollector.renameFilePlace.runAndSetPath(path);
            else
                ComponentsCollector.renameFolderPlace.runAndSetPath(path);
        });

        delete.setOnAction(event -> FileHelper.deleteFile(path));
    }

    private static void checkPath(Path path) {
        isFile(!Files.isDirectory(path));
        isProject(path.equals(Project.path));
    }

    private static void isProject(boolean isTrue) {
        cut.setDisable(isTrue);
        copy.setDisable(isTrue);
        delete.setDisable(isTrue);
    }

    private static void isFile(boolean isTrue) {
        createFile.setDisable(isTrue);
        createFolder.setDisable(isTrue);
        paste.setDisable(isTrue);
    }

    public static void handleRequest(ContextMenuEvent event, Path path) {
        checkPath(path);
        initListeners(path);

        contextMenu.show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
    }
}
