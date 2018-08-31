package ru.mrchebik.gui.place.menu.create.file;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.CellStageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.screen.measurement.Scale;

public class CreateFilePlace extends CellStageHelper {
    public static String setTitle() {
        return Locale.getProperty("create_file_title", true);
    }

    @Override
    public void start() {
        super.stage = new Stage();

        initWindow(setTitle(),
                Modality.APPLICATION_MODAL,
                Scale.PLACE_CREATE_FILE,
                ViewHelper.CREATE_FILE);
    }
}
