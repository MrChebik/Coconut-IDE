package ru.mrchebik.gui.place.menu.rename.folder;

import javafx.stage.Modality;
import ru.mrchebik.gui.place.CellStageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.screen.measurement.Scale;

public class RenameFolderPlace extends CellStageHelper {
    public static String setTitle() {
        return Locale.getProperty("rename_folder_title", true);
    }

    @Override
    public void start() {
        super.stage = ViewHelper.RENAME_FOLDER.stage;

        initWindow(setTitle(),
                Modality.APPLICATION_MODAL,
                Scale.PLACE_RENAME_FOLDER,
                ViewHelper.RENAME_FOLDER);
    }
}
