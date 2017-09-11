package ru.mrchebik.controller.actions.autosave;

/**
 * Created by mrchebik on 9/2/17.
 */
public abstract class Autosave extends Thread {
    @Override
    public void run() {
        save();
    }

    public void save() {
        throw new UnsupportedOperationException();
    }
}
