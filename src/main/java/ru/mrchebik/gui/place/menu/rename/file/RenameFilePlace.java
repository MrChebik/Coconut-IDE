package ru.mrchebik.gui.place.menu.rename.file;

import javafx.stage.Modality;
import ru.mrchebik.gui.place.CellStageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.screen.measurement.Scale;

public class RenameFilePlace extends CellStageHelper {
    public static String setTitle() {
        return Locale.getProperty("rename_file_title", true);
    }

    @Override
    public void start() {
        super.stage = ViewHelper.RENAME_FILE.stage;

        initWindow(setTitle(),
                Modality.APPLICATION_MODAL,
                Scale.PLACE_RENAME_FILE,
                ViewHelper.RENAME_FILE);
    }
}
