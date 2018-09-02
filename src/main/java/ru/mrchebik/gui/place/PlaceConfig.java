package ru.mrchebik.gui.place;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.icons.Icons;
import ru.mrchebik.screen.Screen;
import ru.mrchebik.screen.measurement.Scale;

import java.util.Arrays;

public class PlaceConfig {
    private static Stage stage;
    private static Scale scale;
    private static Parent view;

    public static void initialize(ViewHelper viewHelper) {
        if (!isCreated(viewHelper.stage)) {
            init(viewHelper);
            initWindow();
        }

        show(viewHelper.stage);
        clear();
    }

    public static void init(ViewHelper viewHelper) {
        PlaceConfig.stage = viewHelper.stage;
        PlaceConfig.scale = viewHelper.scale;
        PlaceConfig.view = viewHelper.view.getView();
    }

    public static void clear() {
        PlaceConfig.stage = null;
        PlaceConfig.scale = null;
        PlaceConfig.view = null;
    }

    public static void closeWindow(ViewHelper... helpers) {
        Arrays.stream(helpers).forEach(e -> e.stage.close());
    }

    public static boolean isCreated(Stage stage) {
        return stage.getIcons().size() != 0;
    }

    public static void show(Stage stage) {
        stage.show();
    }

    public static void initWindow() {
        stage.getIcons().add(Icons.LOGO.get());

        if (!scale.equals(Scale.PLACE_START))
            stage.initModality(Modality.APPLICATION_MODAL);

        if (!scale.equals(Scale.PLACE_WORK)) {
            setResizableFalse();
            stage.setResizable(false);
        } else {
            setSize();
            setOnClose(() -> System.exit(0));
        }

        setPosition();

        Scene scene = initScene();
        //initFrame(scene);
        stage.setScene(scene);
    }

    private static  void initFrame(Scene scene) {
        if (scene.getRoot().getChildrenUnmodifiable().get(0) instanceof GridPane) {
            GridPane gridPane = (GridPane) scene.getRoot().getChildrenUnmodifiable().get(0);
            if (gridPane.getChildren().size() > 0 &&
                    gridPane.getChildren().get(1) instanceof TextField) {
                TextField field = (TextField) gridPane.getChildren().get(1);
                field.setText("");
            }
        }
    }

    private static void setOnClose(Runnable handler) {
        stage.setOnCloseRequest(event -> handler.run());
    }

    private static Scene initScene() {
        return view.isNeedsLayout() && view.getScene() == null ?
                new Scene(view)
                :
                view.getScene();
    }

    private static void setResizableFalse() {
        var width = scale.width;
        var height = scale.height;

        stage.setMinWidth(width);
        stage.setMinHeight(height);
    }

    private static void setSize() {
        if (scale.isOverriden())
            scale.init();

        var width = scale.width;
        var height = scale.height;

        stage.setWidth(width);
        stage.setHeight(height);
    }

    private static void setPosition() {
        var point = Screen.calculateCenter(
                scale.width,
                scale.height);

        stage.setX(point.x);
        stage.setY(point.y);
    }
}