package ru.mrchebik.gui.place.create.file;

import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.screen.measurement.Scale;

import java.nio.file.Path;

public class CreateFilePlace extends StageHelper {
    @Getter @Setter
    private Path path;

    public static CreateFilePlace create() {
        return new CreateFilePlace();
    }

    public void start() {
        super.childStage = new Stage();

        var scale = new Scale(400, 150);
        var view = new CreateFileView();

        StageHelper.initWindow(super.childStage,
                "Create File",
                Modality.APPLICATION_MODAL,
                scale,
                view.getView());
    }
}
