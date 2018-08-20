package ru.mrchebik.gui.place.create.project;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.screen.measurement.Scale;

public class CreateProjectPlace extends StageHelper {
    public void start() {
        super.stage = new Stage();

        initWindow(super.stage,
                Locale.NEW_PROJECT_TITLE,
                Modality.APPLICATION_MODAL,
                Scale.PLACE_CREATE_PROJECT,
                ViewHelper.PLACE_CREATE_PROJECT);
    }
}
