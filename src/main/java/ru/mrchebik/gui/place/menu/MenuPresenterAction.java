package ru.mrchebik.gui.place.menu;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.SneakyThrows;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MenuPresenterAction extends KeyHelper {
    protected static void initLocale(Label label,
                                     Button button,
                                     boolean isRename) {
        label.setText(Locale.getProperty("name_label", true) + ":");
        button.setText(isRename ?
                Locale.getProperty("rename_button", true)
                :
                Locale.getProperty("create_button", true));
    }

    protected static void createFile(TextField name,
                                     Path path,
                                     boolean isFile) {
        var needPath = buildPath(name,
                path);
        if (isFile)
            FileUtil.createFile(needPath);
        else
            FileUtil.createFolder(needPath);
    }

    @SneakyThrows(IOException.class)
    protected static void renameFile(TextField name,
                                     Path path) {
        var needPath = buildPath(name,
                path.getParent());
        Files.move(path, needPath);
    }

    private static Path buildPath(TextField field,
                                  Path path) {
        var nameOfFile = field.getText();
        return path.resolve(nameOfFile);
    }
}
