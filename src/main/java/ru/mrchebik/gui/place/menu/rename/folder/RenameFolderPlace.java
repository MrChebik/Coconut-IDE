package ru.mrchebik.gui.place.menu.rename.folder;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.CellStageAction;
import ru.mrchebik.gui.place.ViewAction;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.screen.measurement.Scale;

public class RenameFolderPlace extends CellStageAction {
    public static String setTitle() {
        return Locale.getProperty("rename_folder_title", true);
    }

    @Override
    public void start() {
        super.stage = new Stage();

        initWindow(super.stage,
                setTitle(),
                Modality.APPLICATION_MODAL,
                Scale.PLACE_RENAME_FOLDER,
                ViewAction.PLACE_RENAME_FOLDER);
    }
}
