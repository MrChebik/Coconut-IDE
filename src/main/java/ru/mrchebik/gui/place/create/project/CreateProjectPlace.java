package ru.mrchebik.gui.place.create.project;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.screen.measurement.Scale;

public class CreateProjectPlace extends StageHelper {
    public static String setTitle() {
        return Locale.getProperty("new_project_title", true);
    }

    public void start() {
        super.stage = new Stage();

        initWindow(setTitle(),
                Modality.APPLICATION_MODAL,
                Scale.PLACE_CREATE_PROJECT,
                ViewHelper.CREATE_PROJECT);
    }
}
