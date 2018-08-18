package ru.mrchebik.gui.place.create.folder;

import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.screen.measurement.Scale;

import java.nio.file.Path;

public class CreateFolderPlace extends StageHelper {
    @Getter
    @Setter
    private Path path;

    public static CreateFolderPlace create() {
        return new CreateFolderPlace();
    }

    public void start() {
        super.stage = new Stage();

        initWindow(super.stage,
                "Create Folder",
                Modality.APPLICATION_MODAL,
                Scale.PLACE_CREATE_FOLDER,
                ViewHelper.PLACE_CREATE_FOLDER);
    }
}
