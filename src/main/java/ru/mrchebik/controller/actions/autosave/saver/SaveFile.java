package ru.mrchebik.controller.actions.autosave.saver;

import ru.mrchebik.controller.actions.autosave.Autosave;
import ru.mrchebik.model.controller.actions.autosave.FutureFile;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by mrchebik on 9/2/17.
 */
public class SaveFile extends Autosave {
    private FutureFile futureFile;

    public SaveFile(FutureFile futureFile) {
        this.futureFile = futureFile;
    }

    public void save() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(futureFile.getPath().toFile());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        assert writer != null;
        writer.write(futureFile.getText());
        writer.flush();
        writer.close();
    }
}
