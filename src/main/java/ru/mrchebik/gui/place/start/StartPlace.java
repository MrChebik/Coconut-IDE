package ru.mrchebik.gui.place.start;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.StageAction;
import ru.mrchebik.gui.place.ViewAction;
import ru.mrchebik.injection.Injection;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.screen.measurement.Scale;

public class StartPlace extends StageAction {
    public static String setTitle() {
        return Locale.getProperty("startup", true);
    }

    @Override
    public void start(Stage primaryStage) {
        Injection.initInjection(this);
        initializeGui(primaryStage);
    }

    private void initializeGui(Stage stage) {
        super.stage = stage;

        initWindow(setTitle(),
                Modality.NONE,
                Scale.PLACE_START,
                ViewAction.PLACE_START);
    }
}
