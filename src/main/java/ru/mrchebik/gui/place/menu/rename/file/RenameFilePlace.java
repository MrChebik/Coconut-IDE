package ru.mrchebik.gui.place.menu.rename.file;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.CellStageAction;
import ru.mrchebik.gui.place.ViewAction;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.screen.measurement.Scale;

public class RenameFilePlace extends CellStageAction {
    public static String setTitle() {
        return Locale.getProperty("rename_file_title", true);
    }

    @Override
    public void start() {
        super.stage = new Stage();

        initWindow(super.stage,
                setTitle(),
                Modality.APPLICATION_MODAL,
                Scale.PLACE_RENAME_FILE,
                ViewAction.PLACE_RENAME_FILE);
    }
}
