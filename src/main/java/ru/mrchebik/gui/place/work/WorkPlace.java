package ru.mrchebik.gui.place.work;

import com.airhacks.afterburner.injection.Injector;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import ru.mrchebik.gui.place.create.file.CreateFilePlace;
import ru.mrchebik.gui.place.create.folder.CreateFolderPlace;
import ru.mrchebik.gui.place.rename.file.RenameFilePlace;
import ru.mrchebik.gui.place.rename.folder.RenameFolderPlace;
import ru.mrchebik.icons.Icons;
import ru.mrchebik.model.ActionPlaces;
import ru.mrchebik.process.io.ErrorProcess;
import ru.mrchebik.process.io.ExecutorCommand;
import ru.mrchebik.project.Project;
import ru.mrchebik.screen.Screen;
import ru.mrchebik.screen.measurement.Point;
import ru.mrchebik.screen.measurement.Scale;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class WorkPlace {
    @Getter
    private Stage stage;

    private ErrorProcess errorProcess;
    private ExecutorCommand executorCommand;
    private Project project;

    public void start(String name, Path path) throws IOException {
        initializeExecutorAndError();
        initializeProject(name, path);
        initializeInject();

        stage = new Stage();
        stage.setTitle(Project.getTitle());

        Screen screen = new Screen();
        Scale scale = screen.calculateScale();
        stage.setWidth(scale.getWidth());
        stage.setHeight(scale.getHeight());

        Point point = screen.calculateCenter(stage.getWidth(), stage.getHeight());
        stage.setX(point.getX());
        stage.setY(point.getY());

        stage.getIcons().add(Icons.LOGO.get());

        stage.setOnCloseRequest(event -> System.exit(0));

        WorkView workView = new WorkView();
        Scene scene = new Scene(workView.getView());
        stage.setScene(scene);
        stage.show();
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
        customProperties.put("project", project);
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
