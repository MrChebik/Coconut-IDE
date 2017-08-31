package ru.mrchebik.model;

import javafx.scene.image.Image;
import ru.mrchebik.Main;

/**
 * Created by mrchebik on 8/30/17.
 */
public class CustomIcons {
    public static final Image logo = new Image(String.valueOf(Main.class.getResource("/img/coconut.png")));

    public static final Image folderCollapseImage = new Image(String.valueOf(Main.class.getResource("/icons/folder.png")));
    public static final Image folderExpandImage = new Image(String.valueOf(Main.class.getResource("/icons/folder-open.png")));
    public static final Image fileImage = new Image(String.valueOf(Main.class.getResource("/icons/text-x-generic.png")));
}