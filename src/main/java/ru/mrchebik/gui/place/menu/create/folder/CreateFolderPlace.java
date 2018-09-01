package ru.mrchebik.gui.place.menu.create.folder;

import javafx.stage.Modality;
import ru.mrchebik.gui.place.CellStageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.screen.measurement.Scale;

public class CreateFolderPlace extends CellStageHelper {
    public static String setTitle() {
        return Locale.getProperty("create_folder_title", true);
    }

    @Override
    public void start() {
        super.stage = ViewHelper.CREATE_FOLDER.stage;

        initWindow(setTitle(),
                Modality.APPLICATION_MODAL,
                Scale.PLACE_CREATE_FOLDER,
                ViewHelper.CREATE_FOLDER);
    }
}
