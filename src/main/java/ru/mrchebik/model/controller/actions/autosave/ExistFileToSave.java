package ru.mrchebik.model.controller.actions.autosave;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by mrchebik on 9/2/17.
 */
@AllArgsConstructor
public class ExistFileToSave {
    private Path path;
    private String lines;

    @SneakyThrows(IOException.class)
    public void save() {
        byte[] linesByte = lines.getBytes();

        Files.write(path, linesByte);
    }
}
