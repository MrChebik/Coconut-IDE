package ru.mrchebik.gui.place.create.project;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.screen.measurement.Scale;

public class CreateProjectPlace extends StageHelper {
    public void start() {
        super.childStage = new Stage();

        var scale = new Scale(500, 200);
        var view = new CreateProjectView();

        StageHelper.initWindow(super.childStage,
                "New Project",
                Modality.APPLICATION_MODAL,
                scale,
                view.getView());
    }
}
