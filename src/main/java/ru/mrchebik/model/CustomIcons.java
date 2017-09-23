package ru.mrchebik.model;

import javafx.scene.image.Image;
import lombok.Getter;
import ru.mrchebik.Main;

public class CustomIcons {
    @Getter
    private Image fileImage;
    @Getter
    private Image folderCollapseImage;
    @Getter
    private Image folderExpandImage;
    @Getter
    private Image logo;

    public CustomIcons() {
        fileImage = new Image(String.valueOf(Main.class.getResource("/icons/text-x-generic.png")));
        folderCollapseImage = new Image(String.valueOf(Main.class.getResource("/icons/folder.png")));
        folderExpandImage = new Image(String.valueOf(Main.class.getResource("/icons/folder-open.png")));
        logo = new Image(String.valueOf(Main.class.getResource("/img/coconut.png")));
    }
}