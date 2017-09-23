package ru.mrchebik.gui.place;


import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PresenterHelper {
    protected boolean isEnter(KeyEvent event) {
        return event.getCode() == KeyCode.ENTER;
    }

    protected boolean isBackSpace(KeyEvent event) {
        return event.getCode() == KeyCode.BACK_SPACE;
    }
}
