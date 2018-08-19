package ru.mrchebik.gui.place.create.folder;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.CellStageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.screen.measurement.Scale;

public class CreateFolderPlace extends CellStageHelper {
    @Override
    public void start() {
        super.stage = new Stage();

        initWindow(super.stage,
                "Create Folder",
                Modality.APPLICATION_MODAL,
                Scale.PLACE_CREATE_FOLDER,
                ViewHelper.PLACE_CREATE_FOLDER);
    }
}
