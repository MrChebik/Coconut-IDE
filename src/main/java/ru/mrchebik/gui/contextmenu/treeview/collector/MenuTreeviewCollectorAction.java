package ru.mrchebik.gui.contextmenu.treeview.collector;

import ru.mrchebik.gui.contextmenu.treeview.MenuTreeviewAction;
import ru.mrchebik.model.CommandPath;
import ru.mrchebik.project.Project;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

class MenuTreeviewCollectorAction extends MenuTreeviewAction {
    static void initListeners(Path path) {
        MenuTreeviewAction.path = path;

        MenuTreeviewCollector.createFile.setOnAction(event -> initCreateFile());
        MenuTreeviewCollector.createFolder.setOnAction(event -> initCreateFolder());
        MenuTreeviewCollector.copy.setOnAction(event -> initCopy());
        MenuTreeviewCollector.cut.setOnAction(event -> initCut());
        MenuTreeviewCollector.paste.setOnAction(event -> initPaste());
        MenuTreeviewCollector.rename.setOnAction(event -> initRename());
        MenuTreeviewCollector.delete.setOnAction(event -> initDelete());
    }

    static void checkPath(Path path) {
        isFile(!Files.isDirectory(path));
        isProject(path.equals(Project.path));
    }

    private static void isProject(boolean isTrue) {
        MenuTreeviewCollector.cut.setDisable(isTrue);
        MenuTreeviewCollector.copy.setDisable(isTrue);
        MenuTreeviewCollector.delete.setDisable(isTrue);
    }

    private static void isFile(boolean isTrue) {
        MenuTreeviewCollector.createFile.setDisable(isTrue);
        MenuTreeviewCollector.createFolder.setDisable(isTrue);
        MenuTreeviewCollector.paste.setDisable(Objects.isNull(CommandPath.command) ||
                isTrue ||
                !Files.exists(CommandPath.path));
    }
}
