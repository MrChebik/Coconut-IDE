package ru.mrchebik.gui.key;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyHelper {
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
