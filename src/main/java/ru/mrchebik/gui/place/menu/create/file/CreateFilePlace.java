package ru.mrchebik.gui.place.menu.create.file;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.CellStageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.screen.measurement.Scale;

public class CreateFilePlace extends CellStageHelper {
    @Override
    public void start() {
        super.stage = new Stage();

        initWindow(super.stage,
                Locale.CREATE_FILE_TITLE,
                Modality.APPLICATION_MODAL,
                Scale.PLACE_CREATE_FILE,
                ViewHelper.PLACE_CREATE_FILE);
    }
}
