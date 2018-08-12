package ru.mrchebik.gui.place.start;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.injection.Injection;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.locale.base.BaseLocale;
import ru.mrchebik.screen.measurement.Scale;

public class StartPlace extends StageHelper {
    @Override
    public void start(Stage primaryStage) {
        Injection.initInjection(this);
        initializeGui(primaryStage);
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
