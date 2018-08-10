package ru.mrchebik.gui.place.start;

import com.airhacks.afterburner.injection.Injector;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.locale.base.BaseLocale;
import ru.mrchebik.screen.measurement.Scale;

import java.util.HashMap;

public class StartPlace extends StageHelper {
    @Override
    public void start(Stage primaryStage) {
        initializeInjection();
        initializeGui(primaryStage);
    }

    private void initializeInjection() {
        var injections = new HashMap<>();
        injections.put("startPlace", this);
        Injector.setConfigurationSource(injections::get);
    }

    private void initializeGui(Stage stage) {
        super.stage = stage;

        initWindow(stage,
                Locale.STARTUP + " - " + BaseLocale.PROJECT,
                Modality.NONE,
                Scale.PLACE_START,
                ViewHelper.PLACE_START);
    }
}
