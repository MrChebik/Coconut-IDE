package ru.mrchebik.screen;

import javafx.geometry.Rectangle2D;
import ru.mrchebik.screen.measurement.Point;

public class Screen {
    public static final Rectangle2D bounds = javafx.stage.Screen.getPrimary().getVisualBounds();

    public static Point calculateCenter(double width, double height) {
        var x = (bounds.getWidth() - width) / 2;
        var y = (bounds.getHeight() - height) / 2;

        return new Point(x, y);
    }
}
