package ru.mrchebik.gui.place.work;

import javafx.stage.Modality;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.inject.Injector;
import ru.mrchebik.project.Project;
import ru.mrchebik.screen.measurement.Scale;

import java.nio.file.Path;
import java.nio.file.Paths;

public class WorkPlace extends StageHelper {
    public void start(String name, Path path) {
        initializeProject(name, path);
        Injector.initInjection(this);

        super.stage = ViewHelper.WORK.stage;

        initWindow(Project.getTitle(),
                Modality.APPLICATION_MODAL,
                Scale.PLACE_WORK,
                ViewHelper.WORK);

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
