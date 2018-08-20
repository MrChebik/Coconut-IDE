package ru.mrchebik.gui.collector.contextmenu.treeview;

import lombok.SneakyThrows;
import ru.mrchebik.gui.collector.ComponentsCollector;
import ru.mrchebik.helper.FileHelper;
import ru.mrchebik.model.CommandPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class MenuTreeviewHelper {
    public static Path path;

    public static void initCreateFile() {
        ComponentsCollector.createFilePlace.runAndSetPath(path);
    }

    public static void initCreateFolder() {
        ComponentsCollector.createFolderPlace.runAndSetPath(path);
    }

    public static void initCopy() {
        CommandPath.init("Copy", path);
    }

    public static void initCut() {
        CommandPath.init("Cut", path);
    }

    @SneakyThrows(IOException.class)
    public static void initPaste() {
        Path moveTo = path.resolve(CommandPath.path.getFileName());
        if (isCut(CommandPath.command))
            Files.move(CommandPath.path, moveTo, StandardCopyOption.REPLACE_EXISTING);
        else
            Files.copy(CommandPath.path, moveTo, StandardCopyOption.REPLACE_EXISTING);
    }

    private static boolean isCut(String command) {
        return "Cut".equals(command);
    }

    public static void initRename() {
        if (!Files.isDirectory(path))
            ComponentsCollector.renameFilePlace.runAndSetPath(path);
        else
            ComponentsCollector.renameFolderPlace.runAndSetPath(path);
    }

    public static void initDelete() {
        FileHelper.deleteFile(path);
    }
}
