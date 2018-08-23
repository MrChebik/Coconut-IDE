package ru.mrchebik.gui.place.work;

import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.StageAction;
import ru.mrchebik.gui.place.ViewAction;
import ru.mrchebik.injection.Injection;
import ru.mrchebik.project.Project;
import ru.mrchebik.screen.measurement.Scale;

import java.nio.file.Path;
import java.nio.file.Paths;

public class WorkPlace extends StageAction {
    public void start(String name, Path path) {
        initializeProject(name, path);
        Injection.initInjection(this);

        super.stage = new Stage();

        initWindow(super.stage,
                Project.getTitle(),
                Modality.APPLICATION_MODAL,
                Scale.PLACE_WORK,
                ViewAction.PLACE_WORK);

        setOnClose(() -> System.exit(0));
    }

    private void initializeProject(String name, Path path) {
        Path pathOut = Paths.get(path.toString(), "out");
        Path pathSource = Paths.get(path.toString(), "src");

        Project.init(name, path, pathOut, pathSource);

        if (!Project.isOpen)
            Project.build();
    }
}
