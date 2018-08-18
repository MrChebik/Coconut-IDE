package ru.mrchebik.screen.measurement;

import lombok.AllArgsConstructor;
import ru.mrchebik.screen.Screen;

@AllArgsConstructor
public enum Scale {
    PLACE_START(600, 400),
    PLACE_CREATE_FILE(400, 150),
    PLACE_CREATE_FOLDER(400, 150),
    PLACE_CREATE_PROJECT(500, 175),
    PLACE_RENAME_FILE(400, 150),
    PLACE_RENAME_FOLDER(400, 150),
    PLACE_WORK() {
        @Override
        public void init() {
            width = Screen.bounds.getWidth() / 100 * 75;
            height = Screen.bounds.getHeight() / 100 * 75;
        }

        @Override
        public boolean isOverriden() {
            return true;
        }
    };

    public double width;
    public double height;

    Scale() {
    }

    public void init() {
        throw new UnsupportedOperationException();
    }

    public boolean isOverriden() {
        return false;
    }
}
