package ru.mrchebik.gui.place.rename.folder;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.CellStageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.screen.measurement.Scale;

public class RenameFolderPlace extends CellStageHelper {
    @Override
    public void start() {
        super.stage = new Stage();

        initWindow(super.stage,
                "Rename Folder",
                Modality.APPLICATION_MODAL,
                Scale.PLACE_RENAME_FOLDER,
                ViewHelper.PLACE_RENAME_FOLDER);
    }
}
