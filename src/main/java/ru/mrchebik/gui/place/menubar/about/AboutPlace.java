package ru.mrchebik.gui.place.menubar.about;

import javafx.stage.Modality;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.screen.measurement.Scale;

public class AboutPlace extends StageHelper {
    public void start() {
        super.stage = ViewHelper.ABOUT.stage;

        initWindow("About",
                Modality.APPLICATION_MODAL,
                Scale.PLACE_MENU_ABOUT,
                ViewHelper.ABOUT);
    }
}
