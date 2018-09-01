package ru.mrchebik.gui.place.start;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.inject.Injector;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.screen.measurement.Scale;

public class StartPlace extends StageHelper {
    public static String setTitle() {
        return Locale.getProperty("startup", true);
    }

    @Override
    public void start(Stage primaryStage) {
        Injector.initInjection(this);
        initializeGui();
    }

    public void start() {
        Injector.initInjection(this);
        initializeGui();
    }

    private void initializeGui() {
        super.stage = ViewHelper.START.stage;

        initWindow(setTitle(),
                Modality.NONE,
                Scale.PLACE_START,
                ViewHelper.START);
    }
}
