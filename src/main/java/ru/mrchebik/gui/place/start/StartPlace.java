package ru.mrchebik.gui.place.start;

import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.screen.measurement.Scale;

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

        var scale = new Scale(600, 400);
        var view = new StartView();

        StageHelper.initWindow(stage,
                "Coconut-IDE",
                Modality.NONE,
                scale,
                view.getView());
    }
}
