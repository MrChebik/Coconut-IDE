package ru.mrchebik.gui.place.create.project;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.screen.measurement.Scale;

public class CreateProjectPlace extends StageHelper {
    public void start() {
        super.stage = new Stage();

        var view = new CreateProjectView();
        initWindow(super.stage,
                "New Project",
                Modality.APPLICATION_MODAL,
                Scale.PLACE_CREATE_PROJECT,
                view.getView());
    }
}
