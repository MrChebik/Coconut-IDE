package ru.mrchebik.model;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@AllArgsConstructor
public class ExistFileToSave {
    private String lines;
    private Path path;

    @SneakyThrows(IOException.class)
    public void save() {
        var linesByte = lines.getBytes();
        Files.write(path, linesByte);
    }
}
