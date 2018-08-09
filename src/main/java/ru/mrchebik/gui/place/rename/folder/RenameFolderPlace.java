package ru.mrchebik.gui.place.rename.folder;

import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.screen.measurement.Scale;

import java.nio.file.Path;

public class RenameFolderPlace extends StageHelper {
    @Getter @Setter
    private Path path;

    public static RenameFolderPlace create() {
        return new RenameFolderPlace();
    }

    public void start() {
        super.childStage = new Stage();

        var scale = new Scale(400, 150);
        var view = new RenameFolderView();

        StageHelper.initWindow(super.childStage,
                "Rename Folder",
                Modality.APPLICATION_MODAL,
                scale,
                view.getView());
    }
}
