package ru.mrchebik.gui.place.start;

import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.screen.Screen;
import ru.mrchebik.model.screen.measurement.Point;

import java.util.HashMap;
import java.util.Map;

public class StartPlace extends Application {
    @Getter
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeInjection();

        stage = primaryStage;
        stage.setTitle("Coconut-IDE");
        stage.setMinWidth(600);
        stage.setMaxWidth(600);
        stage.setMinHeight(400);
        stage.setMaxHeight(400);

        Screen screen = new Screen();
        Point point = screen.calculateCenter(600, 400);
        stage.setX(point.getX());
        stage.setY(point.getY());

        CustomIcons customIcons = new CustomIcons();
        stage.getIcons().add(customIcons.getLogo());

        StartView startView = new StartView();
        Scene scene = new Scene(startView.getView());
        stage.setScene(scene);
        stage.show();
    }

    public void close() {
        stage.close();
    }

    private void initializeInjection() {
        Map<Object, Object> customProperties = new HashMap<>();
        customProperties.put("startPlace", this);
        Injector.setConfigurationSource(customProperties::get);
    }
}
