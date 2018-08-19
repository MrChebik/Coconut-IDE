package ru.mrchebik.gui.place.create.file;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.CellStageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.screen.measurement.Scale;

public class CreateFilePlace extends CellStageHelper {
    @Override
    public void start() {
        super.stage = new Stage();

        initWindow(super.stage,
                "Create File",
                Modality.APPLICATION_MODAL,
                Scale.PLACE_CREATE_FILE,
                ViewHelper.PLACE_CREATE_FILE);
    }
}
