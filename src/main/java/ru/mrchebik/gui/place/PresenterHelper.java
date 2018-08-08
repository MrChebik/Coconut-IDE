package ru.mrchebik.gui.place;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class PresenterHelper {
    public static boolean isEnter(KeyEvent event) {
        return event.getCode() == KeyCode.ENTER;
    }
    public static boolean isBackSpace(KeyEvent event) {
        return event.getCode() == KeyCode.BACK_SPACE;
    }
    public static boolean isSemicolon(KeyEvent event) {
        return event.getCode() == KeyCode.SEMICOLON;
    }
}
