package ru.mrchebik.gui.node.button;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class CustomButton extends Button {
    private static final String CSS = "-fx-font-family: Arkhip;" +
            "-fx-font-size: 14;" +
            "-fx-background-color: #795548;" +
            "-fx-background-radius: 2px;" +
            "-fx-padding: 6 20 6 20;" +
            "-fx-text-fill: whitesmoke;";

    private static final String DEFAULT_INSETS =
            "-fx-background-insets: 0 0 -1 0, 0, 1, 2;";

    private final Timeline timeShadowUp;
    private final Timeline timeShadowDown;

    public CustomButton() {
        super();
        DropShadow dropShadow0 = new DropShadow(3, 0, 1, Color.rgb(0, 0, 0, 0.12));
        DropShadow dropShadow1 = new DropShadow(2, 0, 1, Color.rgb(0, 0, 0, 0.24));
        dropShadow0.setInput(dropShadow1);
        boolean[] focused = new boolean[1];
        focused[0] = this.isFocused();

        timeShadowUp = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new KeyValue(dropShadow0.offsetYProperty(), 6,  Interpolator.EASE_BOTH),
                        new KeyValue(dropShadow0.radiusProperty(), 16, Interpolator.EASE_BOTH),
                        new KeyValue(dropShadow0.colorProperty(), Color.rgb(0, 0, 0,0.30), Interpolator.EASE_BOTH),

                        new KeyValue(dropShadow1.offsetYProperty(), 4,  Interpolator.EASE_BOTH),
                        new KeyValue(dropShadow1.radiusProperty(), 8, Interpolator.EASE_BOTH),
                        new KeyValue(dropShadow1.colorProperty(), Color.rgb(0, 0, 0, /*0.36*/0.48), Interpolator.EASE_BOTH))
        );

        timeShadowDown = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new KeyValue(dropShadow0.offsetYProperty(), 1,  Interpolator.EASE_BOTH),
                        new KeyValue(dropShadow0.radiusProperty(), 3, Interpolator.EASE_BOTH),
                        new KeyValue(dropShadow0.colorProperty(), Color.rgb(0, 0, 0, 0.12), Interpolator.EASE_BOTH),

                        new KeyValue(dropShadow1.offsetYProperty(), 1,  Interpolator.EASE_BOTH),
                        new KeyValue(dropShadow1.radiusProperty(), 2, Interpolator.EASE_BOTH),
                        new KeyValue(dropShadow1.colorProperty(), Color.rgb(0, 0, 0, 0.24), Interpolator.EASE_BOTH))
        );

        this.setStyle(CSS);
        this.setEffect(dropShadow0);
        this.hoverProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue)
                timeShadowUp.play();
            else if (!focused[0])
                timeShadowDown.play();
        }));
        this.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                this.setStyle(CSS + DEFAULT_INSETS);
                timeShadowUp.play();
            } else
                timeShadowDown.play();
            focused[0] = newValue;
        }));
        this.setOnMouseClicked(event -> {
            // TODO ripple
        });
    }
}
