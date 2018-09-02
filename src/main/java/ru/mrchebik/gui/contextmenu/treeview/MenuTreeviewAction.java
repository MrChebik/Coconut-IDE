package ru.mrchebik.gui.contextmenu.treeview;

import lombok.SneakyThrows;
import ru.mrchebik.gui.contextmenu.treeview.collector.MenuTreeviewCollector;
import ru.mrchebik.gui.place.CellPlaceConfig;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.model.CommandPath;
import ru.mrchebik.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class MenuTreeviewAction extends MenuTreeviewCollector {
    protected static Path path;

    protected static void initCreateFile() {
        CellPlaceConfig.runAndSetPath(path, ViewHelper.CREATE_FILE);
    }

    protected static void initCreateFolder() {
        CellPlaceConfig.runAndSetPath(path, ViewHelper.CREATE_FOLDER);
    }

    protected static void initCopy() {
        CommandPath.init("Copy", path);
    }

    protected static void initCut() {
        CommandPath.init("Cut", path);
    }

    @SneakyThrows(IOException.class)
    protected static void initPaste() {
        Path moveTo = path.resolve(CommandPath.path.getFileName());
        if (isCut(CommandPath.command))
            Files.move(CommandPath.path, moveTo, StandardCopyOption.REPLACE_EXISTING);
        else
            Files.copy(CommandPath.path, moveTo, StandardCopyOption.REPLACE_EXISTING);
    }

    private static boolean isCut(String command) {
        return "Cut".equals(command);
    }

    protected static void initRename() {
        if (!Files.isDirectory(path))
            CellPlaceConfig.runAndSetPath(path, ViewHelper.RENAME_FILE);
        else
            CellPlaceConfig.runAndSetPath(path, ViewHelper.RENAME_FOLDER);
    }

    protected static void initDelete() {
        FileUtil.deleteFile(path);
    }
}
