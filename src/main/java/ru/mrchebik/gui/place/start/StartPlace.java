package ru.mrchebik.gui.place.start;

import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import ru.mrchebik.icons.Icons;
import ru.mrchebik.screen.Screen;

import java.util.HashMap;

public class StartPlace extends Application {
    @Getter
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        initializeInjection();
        initializeGui(primaryStage);
    }

    public void close() {
        stage.close();
    }

    private void initializeInjection() {
        var injections = new HashMap<>();
        injections.put("startPlace", this);
        Injector.setConfigurationSource(injections::get);
    }

    private void initializeGui(Stage stage) {
        this.stage = stage;

        stage.setTitle("Coconut-IDE");
        stage.getIcons().add(Icons.LOGO.get());
        this.setResizableFalse(stage, 600, 400);
        this.setPosition(stage, 600, 400);

        var startView = new StartView();
        var scene = new Scene(startView.getView());
        stage.setScene(scene);
        stage.show();
    }

    private void setResizableFalse(Stage stage, int width, int height) {
        stage.setMinWidth(width);
        stage.setMaxWidth(width);
        stage.setMinHeight(height);
        stage.setMaxHeight(height);
    }

    private void setPosition(Stage stage, int width, int height) {
        var point = Screen.calculateCenter(width, height);

        stage.setX(point.getX());
        stage.setY(point.getY());
    }
}
