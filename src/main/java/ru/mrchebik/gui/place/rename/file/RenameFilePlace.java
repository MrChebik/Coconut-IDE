package ru.mrchebik.gui.place.rename.file;

import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.screen.measurement.Scale;

import java.nio.file.Path;

public class RenameFilePlace extends StageHelper {
    @Getter @Setter
    private Path path;

    public static RenameFilePlace create() {
        return new RenameFilePlace();
    }

    public void start() {
        super.stage = new Stage();

        initWindow(super.stage,
                "Rename File",
                Modality.APPLICATION_MODAL,
                Scale.PLACE_RENAME_FILE,
                ViewHelper.PLACE_RENAME_FILE);
    }
}
