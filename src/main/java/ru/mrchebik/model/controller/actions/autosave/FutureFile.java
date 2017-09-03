package ru.mrchebik.model.controller.actions.autosave;

import java.nio.file.Path;

/**
 * Created by mrchebik on 9/2/17.
 */
public class FutureFile {
    private Path path;
    private String text;

    public FutureFile(Path path, String text) {
        this.path = path;
        this.text = text;
    }

    public Path getPath() {
        return path;
    }

    public String getText() {
        return text;
    }
}
