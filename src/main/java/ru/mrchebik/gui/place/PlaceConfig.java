package ru.mrchebik.gui.place;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.mrchebik.gui.place.start.StartPresenter;
import ru.mrchebik.gui.place.work.WorkPresenter;
import ru.mrchebik.gui.titlebar.TitlebarPresenter;
import ru.mrchebik.icons.Icons;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.project.Project;
import ru.mrchebik.screen.Screen;
import ru.mrchebik.screen.measurement.Scale;

import java.util.Arrays;

public class PlaceConfig {
    private static Stage stage;
    private static Scale scale;
    private static Parent view;
    private static String key;

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
        PlaceConfig.key = viewHelper.key;
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
        stage.initStyle(StageStyle.UNDECORATED);
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

        setTitle(key);
    }

    private static void setTitle(String key) {
        String title = scale == Scale.PLACE_WORK ?
                Project.getTitle()
                :
                Locale.getProperty(key, true);
        TitlebarPresenter presenter = (TitlebarPresenter) ViewHelper.TITLE.view.getPresenter();
        if (scale == Scale.PLACE_START || scale == Scale.PLACE_WORK) {
            TitlebarPresenter.stage = stage;
            presenter.title.setText(title);
            if (scale == Scale.PLACE_START) {
                StartPresenter startPresenter = (StartPresenter) ViewHelper.START.view.getPresenter();
                startPresenter.titleZone.setTop(presenter.titlebar);
            } else if (scale == Scale.PLACE_WORK) {
                WorkPresenter workPresenter = (WorkPresenter) ViewHelper.WORK.view.getPresenter();
                workPresenter.titleZone.setTop(presenter.titlebar);
            }
        }

        stage.setTitle(title);
    }

    private static void initFrame(Scene scene) {
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
