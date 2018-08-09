package ru.mrchebik.gui.place.rename.file;

import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.screen.measurement.Scale;

import java.nio.file.Path;

public class RenameFilePlace extends StageHelper {
    @Getter @Setter
    private Path path;

    public static RenameFilePlace create() {
        return new RenameFilePlace();
    }

    public void start() {
        super.childStage = new Stage();

        var scale = new Scale(400, 150);
        var view = new RenameFileView();

        StageHelper.initWindow(super.childStage,
                "Rename File",
                Modality.APPLICATION_MODAL,
                scale,
                view.getView());
    }
}
