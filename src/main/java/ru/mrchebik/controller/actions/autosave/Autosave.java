package ru.mrchebik.controller.actions.autosave;

import java.io.FileNotFoundException;

/**
 * Created by mrchebik on 9/2/17.
 */
public abstract class Autosave extends Thread {
    @Override
    public void run() {
        try {
            save();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save() throws FileNotFoundException {
        throw new UnsupportedOperationException();
    }
}
