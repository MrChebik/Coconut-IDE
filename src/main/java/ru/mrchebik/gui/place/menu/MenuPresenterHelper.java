package ru.mrchebik.gui.place.menu;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.SneakyThrows;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.helper.FileHelper;
import ru.mrchebik.locale.Locale;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MenuPresenterHelper extends KeyHelper {
    protected static void initLocale(Label label,
                                     Button button,
                                     boolean isRename) {
        label.setText(Locale.NAME_LABEL + ":");
        button.setText(isRename ?
                Locale.RENAME_BUTTON
                :
                Locale.CREATE_BUTTON);
    }

    protected static void createFile(TextField name,
                                     Path path,
                                     boolean isFile) {
        var needPath = buildPath(name,
                path);
        if (isFile)
            FileHelper.createFile(needPath);
        else
            FileHelper.createFolder(needPath);
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
