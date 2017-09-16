package ru.mrchebik.gui.places.start;

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

/**
 * Created by mrchebik on 8/29/17.
 */
public class StartPlace extends Application {
    private @Getter static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("Coconut-IDE");
        stage.setWidth(600);
        stage.setHeight(400);
        stage.setResizable(false);

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

        initializeInjection();
    }

    private void initializeInjection() {
        Map<Object, Object> customProperties = new HashMap<>();
        customProperties.put("startPlace", this);
        Injector.setConfigurationSource(customProperties::get);
    }

    public void close() {
        stage.close();
    }
}
