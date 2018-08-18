package ru.mrchebik.gui.place.rename.folder;

import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.screen.measurement.Scale;

import java.nio.file.Path;

public class RenameFolderPlace extends StageHelper {
    @Getter
    @Setter
    private Path path;

    public static RenameFolderPlace create() {
        return new RenameFolderPlace();
    }

    public void start() {
        super.stage = new Stage();

        initWindow(super.stage,
                "Rename Folder",
                Modality.APPLICATION_MODAL,
                Scale.PLACE_RENAME_FOLDER,
                ViewHelper.PLACE_RENAME_FOLDER);
    }
}
