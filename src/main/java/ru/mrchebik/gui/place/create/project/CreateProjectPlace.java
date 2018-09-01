package ru.mrchebik.gui.place.create.project;

import javafx.stage.Modality;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.screen.measurement.Scale;

public class CreateProjectPlace extends StageHelper {
    public static String setTitle() {
        return Locale.getProperty("new_project_title", true);
    }

    public void start() {
        super.stage = ViewHelper.CREATE_PROJECT.stage;

        initWindow(setTitle(),
                Modality.APPLICATION_MODAL,
                Scale.PLACE_CREATE_PROJECT,
                ViewHelper.CREATE_PROJECT);
    }
}
