package ru.mrchebik.gui.place;

import java.nio.file.Path;

public class CellPlaceConfig extends PlaceConfig {
    public static Path path;

    public static Path closeAndGetPath(ViewHelper helper) {
        PlaceConfig.closeWindow(helper);

        return path;
    }

    public static void runAndSetPath(Path path, ViewHelper helper) {
        CellPlaceConfig.path = path;

        helper.start();
    }
}
