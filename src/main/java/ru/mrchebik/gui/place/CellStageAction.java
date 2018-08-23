package ru.mrchebik.gui.place;

import java.nio.file.Path;

public class CellStageAction extends StageAction {
    public Path path;

    protected void start() {
        throw new UnsupportedOperationException();
    }

    public Path closeAndGetPath() {
        StageAction.closeWindow(this);

        return path;
    }

    public void runAndSetPath(Path path) {
        this.path = path;

        start();
    }
}
