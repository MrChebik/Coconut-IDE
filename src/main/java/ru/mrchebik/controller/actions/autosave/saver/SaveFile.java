package ru.mrchebik.controller.actions.autosave.saver;

import ru.mrchebik.controller.actions.autosave.Autosave;
import ru.mrchebik.model.controller.actions.autosave.ExistFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by mrchebik on 9/2/17.
 */
public class SaveFile extends Autosave {
    private ExistFile existFile;

    public SaveFile(ExistFile existFile) {
        this.existFile = existFile;
    }

    public void save() {
        File file = existFile.getPath().toFile();

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
        } catch (FileNotFoundException e1) {
            // TODO show error window
        }

        if (writer != null) {
            writer.write(existFile.getText());
            writer.flush();
            writer.close();
        }
    }
}
