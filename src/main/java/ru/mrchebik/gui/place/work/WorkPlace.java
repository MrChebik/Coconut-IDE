package ru.mrchebik.gui.place.work;

import com.airhacks.afterburner.injection.Injector;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.gui.place.create.file.CreateFilePlace;
import ru.mrchebik.gui.place.create.folder.CreateFolderPlace;
import ru.mrchebik.gui.place.rename.file.RenameFilePlace;
import ru.mrchebik.gui.place.rename.folder.RenameFolderPlace;
import ru.mrchebik.model.ActionPlaces;
import ru.mrchebik.process.io.ErrorProcess;
import ru.mrchebik.process.io.ExecutorCommand;
import ru.mrchebik.project.Project;
import ru.mrchebik.screen.measurement.Scale;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class WorkPlace extends StageHelper {
    private ErrorProcess errorProcess;
    private ExecutorCommand executorCommand;

    public void start(String name, Path path) {
        initializeExecutorAndError();
        initializeProject(name, path);
        initializeInject();

        super.stage = new Stage();

        initWindow(super.stage,
                Project.getTitle(),
                Modality.APPLICATION_MODAL,
                Scale.PLACE_WORK,
                ViewHelper.PLACE_WORK);

        setOnClose(() -> System.exit(0));
    }

    private void initializeExecutorAndError() {
        executorCommand = ExecutorCommand.create();
        errorProcess = ErrorProcess.create();
    }

    private void initializeInject() {
        ActionPlaces places = new ActionPlaces(
                CreateFilePlace.create(),
                CreateFolderPlace.create(),
                RenameFilePlace.create(),
                RenameFolderPlace.create(),
                this);

        Map<Object, Object> customProperties = new HashMap<>();
        customProperties.put("executorCommand", executorCommand);
        customProperties.put("errorProcess", errorProcess);
        customProperties.put("places", places);
        Injector.setConfigurationSource(customProperties::get);
    }

    private void initializeProject(String name, Path path) {
        Path pathOut = Paths.get(path.toString(), "out");
        Path pathSource = Paths.get(path.toString(), "src");

        /*project = new Project(name, path, pathOut, pathSource, errorProcess, executorCommand);
        project.build();*/
    }
}
