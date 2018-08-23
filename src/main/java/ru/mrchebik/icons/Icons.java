package ru.mrchebik.icons;

import javafx.scene.image.Image;
import ru.mrchebik.Main;

public enum Icons {
    FILE("/icons/text-x-generic.png"),
    FOLDER_COLLAPSE("/icons/folder.png"),
    FOLDER_EXPAND("/icons/folder-open.png"),
    LOGO("/img/coconut.png");

    private Image image;

    Icons(String path) {
        this.image = new Image(String.valueOf(Main.class.getResource(path)));
    }

    public Image get() {
        return image;
    }
}