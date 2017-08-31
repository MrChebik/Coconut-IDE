package ru.mrchebik.model;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * Created by mrchebik on 8/31/17.
 */
public class ScreenInfo {
    public static final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
}
