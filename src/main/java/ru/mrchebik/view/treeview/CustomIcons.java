package ru.mrchebik.view.treeview;

import javafx.scene.image.Image;
import ru.mrchebik.controller.javafx.WorkStationController;

/**
 * Created by mrchebik on 8/30/17.
 */
public class CustomIcons {
    public static Image folderCollapseImage = new Image(String.valueOf(WorkStationController.class.getResource("/icons/folder.png")));
    public static Image folderExpandImage = new Image(String.valueOf(WorkStationController.class.getResource("/icons/folder-open.png")));
    public static Image fileImage = new Image(String.valueOf(WorkStationController.class.getResource("/icons/text-x-generic.png")));
}
