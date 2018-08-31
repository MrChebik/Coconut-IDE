package ru.mrchebik.gui.place;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import ru.mrchebik.icons.Icons;
import ru.mrchebik.screen.Screen;
import ru.mrchebik.screen.measurement.Scale;

import java.util.Arrays;

public class StageHelper extends Application {
    @Getter
    protected Stage stage;

    public static void closeWindow(StageHelper... helpers) {
        Arrays.stream(helpers).forEach(StageHelper::close);
    }

    protected void initWindow(String title,
                              Modality modality,
                              Scale scale,
                              ViewHelper viewHelper) {
        stage.setTitle(title);
        stage.getIcons().add(Icons.LOGO.get());

        if (!modality.equals(Modality.NONE))
            stage.initModality(modality);

        if (!scale.equals(Scale.PLACE_WORK)) {
            setResizableFalse(stage, scale);
            stage.setResizable(false);
        } else
            setSize(stage, scale);

        setPosition(stage, scale);

        Scene scene = initScene(viewHelper);
        initFrame(scene);
        stage.setScene(scene);
        stage.show();
    }

    private void initFrame(Scene scene) {
        if (scene.getRoot().getChildrenUnmodifiable().get(0) instanceof GridPane) {
            GridPane gridPane = (GridPane) scene.getRoot().getChildrenUnmodifiable().get(0);
            if (gridPane.getChildren().size() > 0 &&
                    gridPane.getChildren().get(1) instanceof TextField) {
                TextField field = (TextField) gridPane.getChildren().get(1);
                field.setText("");
            }
        }
    }

    protected void setOnClose(Runnable handler) {
        stage.setOnCloseRequest(event -> handler.run());
    }

    private Scene initScene(ViewHelper viewHelper) {
        Parent view = viewHelper.view.getView();

        return view.isNeedsLayout() ?
                new Scene(view)
                :
                view.getScene();
    }

    @Override
    public void start(Stage primaryStage) {
        throw new UnsupportedOperationException();
    }

    private void setResizableFalse(Stage stage,
                                   Scale scale) {
        var width = scale.width;
        var height = scale.height;

        stage.setMinWidth(width);
        stage.setMinHeight(height);
    }

    private void setSize(Stage stage,
                         Scale scale) {
        if (scale.isOverriden())
            scale.init();

        var width = scale.width;
        var height = scale.height;

        stage.setWidth(width);
        stage.setHeight(height);
    }

    private void setPosition(Stage stage,
                             Scale scale) {
        var point = Screen.calculateCenter(scale.width,
                scale.height);

        stage.setX(point.x);
        stage.setY(point.y);
    }

    private void close() {
        stage.close();
    }
}
