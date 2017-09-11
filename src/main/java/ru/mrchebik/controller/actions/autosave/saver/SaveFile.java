package ru.mrchebik.controller.actions.autosave.saver;

import ru.mrchebik.controller.actions.autosave.Autosave;
import ru.mrchebik.model.controller.actions.autosave.ExistFileToSave;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by mrchebik on 9/2/17.
 */
public class SaveFile extends Autosave {
    private ExistFileToSave existFileToSave;

    public SaveFile(ExistFileToSave existFileToSave) {
        this.existFileToSave = existFileToSave;
    }

    public void save() {
        Path path = existFileToSave.getPath();
        String lines = existFileToSave.getLines();
        byte[] linesByte = lines.getBytes();

        try {
            Files.write(path, linesByte);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO show error window
        }
    }
}
