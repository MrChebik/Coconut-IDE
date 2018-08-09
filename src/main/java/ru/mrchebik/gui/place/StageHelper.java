package ru.mrchebik.gui.place;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import ru.mrchebik.icons.Icons;
import ru.mrchebik.screen.Screen;
import ru.mrchebik.screen.measurement.Scale;

public class StageHelper {
    @Getter
    protected Stage childStage;

    public void close() {
        childStage.close();
    }

    public static void initWindow(Stage stage, String title, Modality modality, Scale scale, Parent view) {
        stage.setTitle(title);
        stage.getIcons().add(Icons.LOGO.get());

        if (!modality.equals(Modality.NONE))
            stage.initModality(modality);

        setResizableFalse(stage, scale);
        setPosition(stage, scale);

        var scene = new Scene(view);
        stage.setScene(scene);
        stage.show();
    }

    public static void setResizableFalse(Stage stage, Scale scale) {
        var width = scale.getWidth();
        var height = scale.getHeight();

        stage.setMinWidth(width);
        stage.setMaxWidth(width);
        stage.setMinHeight(height);
        stage.setMaxHeight(height);
    }

    public static void setPosition(Stage stage, Scale scale) {
        var point = Screen.calculateCenter(scale.getWidth(),
                scale.getHeight());

        stage.setX(point.getX());
        stage.setY(point.getY());
    }
}
