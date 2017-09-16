package ru.mrchebik.model;

import javafx.scene.image.Image;
import lombok.Getter;
import ru.mrchebik.Main;

/**
 * Created by mrchebik on 8/30/17.
 */
public class CustomIcons {
    private @Getter Image logo = new Image(String.valueOf(Main.class.getResource("/img/coconut.png")));

    private @Getter Image folderCollapseImage = new Image(String.valueOf(Main.class.getResource("/icons/folder.png")));
    private @Getter Image folderExpandImage = new Image(String.valueOf(Main.class.getResource("/icons/folder-open.png")));
    private @Getter Image fileImage = new Image(String.valueOf(Main.class.getResource("/icons/text-x-generic.png")));
}