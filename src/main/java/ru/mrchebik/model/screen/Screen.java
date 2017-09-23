package ru.mrchebik.model.screen;

import javafx.geometry.Rectangle2D;
import ru.mrchebik.model.screen.measurement.Point;
import ru.mrchebik.model.screen.measurement.Scale;

public class Screen {
    private Rectangle2D bounds;

    public Screen() {
        bounds = javafx.stage.Screen.getPrimary().getVisualBounds();
    }

    public Point calculateCenter(double width, double height) {
        double x = (bounds.getWidth() - width) / 2;
        double y = (bounds.getHeight() - height) / 2;

        return new Point(x, y);
    }

    public Scale calculateScale() {
        double width = bounds.getWidth() / 100 * 75;
        double height = bounds.getHeight() / 100 * 75;

        return new Scale(width, height);
    }
}
