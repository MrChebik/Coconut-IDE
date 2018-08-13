package ru.mrchebik.screen.measurement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
        public void init() {
            setWidth(Screen.bounds.getWidth() / 100 * 75);
            setHeight(Screen.bounds.getHeight() / 100 * 75);
        }
    };

    Scale() {
    }

    @Getter @Setter
    private double width;
    @Getter @Setter
    private double height;
}
