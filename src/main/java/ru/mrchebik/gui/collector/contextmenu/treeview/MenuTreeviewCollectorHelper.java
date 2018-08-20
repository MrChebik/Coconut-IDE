package ru.mrchebik.gui.collector.contextmenu.treeview;

import ru.mrchebik.model.CommandPath;
import ru.mrchebik.project.Project;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

class MenuTreeviewCollectorHelper {
    static void initListeners(Path path) {
        MenuTreeviewHelper.path = path;

        MenuTreeviewCollector.createFile.setOnAction(event -> MenuTreeviewHelper.initCreateFile());
        MenuTreeviewCollector.createFolder.setOnAction(event -> MenuTreeviewHelper.initCreateFolder());
        MenuTreeviewCollector.copy.setOnAction(event -> MenuTreeviewHelper.initCopy());
        MenuTreeviewCollector.cut.setOnAction(event -> MenuTreeviewHelper.initCut());
        MenuTreeviewCollector.paste.setOnAction(event -> MenuTreeviewHelper.initPaste());
        MenuTreeviewCollector.rename.setOnAction(event -> MenuTreeviewHelper.initRename());
        MenuTreeviewCollector.delete.setOnAction(event -> MenuTreeviewHelper.initDelete());
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
