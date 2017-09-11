package ru.mrchebik.model;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import lombok.Getter;

/**
 * Created by mrchebik on 8/31/17.
 */
public class ScreenInfo {
    private @Getter
    static Rectangle2D BOUNDS = Screen.getPrimary().getVisualBounds();
}
