package ru.mrchebik.gui.place;

import java.nio.file.Path;

public class CellStageHelper extends StageHelper {
    public Path path;

    protected void start() {
        throw new UnsupportedOperationException();
    }

    public Path closeAndGetPath() {
        StageHelper.closeWindow(this);

        return path;
    }

    public void runAndSetPath(Path path) {
        this.path = path;

        start();
    }
}
