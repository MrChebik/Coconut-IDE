package ru.mrchebik.gui.place;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import ru.mrchebik.icons.Icons;
import ru.mrchebik.screen.Screen;
import ru.mrchebik.screen.measurement.Scale;

public class StageHelper extends Application {
    @Getter
    protected Stage stage;

    protected void initWindow(Stage      stage,
                              String     title,
                              Modality   modality,
                              Scale      scale,
                              ViewHelper view) {
        stage.setTitle(title);
        stage.getIcons().add(Icons.LOGO.get());

        if (!modality.equals(Modality.NONE))
            stage.initModality(modality);

        if (!scale.equals(Scale.PLACE_WORK)) {
            setResizableFalse(stage, scale);
            stage.setResizable(false);
        } else {
            setSize(stage, scale);
        }

        setPosition(stage, scale);

        var scene = new Scene(view.getView().getView());
        stage.setScene(scene);
        stage.show();
    }

    protected void setOnClose(Runnable handler) {
        stage.setOnCloseRequest(event -> handler.run());
    }

    public void close() {
        stage.close();
    }

    @Override
    public void start(Stage primaryStage) {
        throw new UnsupportedOperationException();
    }

    private void setResizableFalse(Stage stage,
                                   Scale scale) {
        var width = scale.getWidth();
        var height = scale.getHeight();

        stage.setMinWidth(width);
        stage.setMinHeight(height);
    }

    private void setSize(Stage stage,
                         Scale scale) {
        if (scale.isOverriden())
            scale.init();

        var width = scale.getWidth();
        var height = scale.getHeight();

        stage.setWidth(width);
        stage.setHeight(height);
    }

    private void setPosition(Stage stage,
                             Scale scale) {
        var point = Screen.calculateCenter(scale.getWidth(),
                scale.getHeight());

        stage.setX(point.getX());
        stage.setY(point.getY());
    }
}
