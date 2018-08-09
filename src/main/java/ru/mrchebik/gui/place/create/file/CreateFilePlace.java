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
        super.stage = new Stage();

        var view = new CreateFileView();
        initWindow(super.stage,
                "Create File",
                Modality.APPLICATION_MODAL,
                Scale.PLACE_CREATE_FILE,
                view.getView());
    }
}
