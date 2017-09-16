package ru.mrchebik.gui.places.work;

import com.airhacks.afterburner.injection.Injector;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.mrchebik.gui.places.creator.object.ObjectPlace;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.Project;
import ru.mrchebik.model.screen.Screen;
import ru.mrchebik.model.screen.measurement.Point;
import ru.mrchebik.model.screen.measurement.Scale;
import ru.mrchebik.process.ExecutorCommand;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mrchebik on 8/29/17.
 */
public class WorkPlace {
    private ExecutorCommand executorCommand;
    public void start(String name, Path path) throws IOException {
        executorCommand = new ExecutorCommand();
        Project project = initalizeProject(name, path);
        initalizeInject(project);

        Stage primaryStage = new Stage();
        primaryStage.setTitle(project.getTitle());

        Screen screen = new Screen();
        Scale scale = screen.calculateScale();
        primaryStage.setWidth(scale.getWidth());
        primaryStage.setHeight(scale.getHeight());

        Point point = screen.calculateCenter(primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setX(point.getX());
        primaryStage.setY(point.getY());

        CustomIcons customIcons = new CustomIcons();
        primaryStage.getIcons().add(customIcons.getLogo());

        primaryStage.setOnCloseRequest(event -> System.exit(0));

        WorkView workView = new WorkView();
        Scene scene = new Scene(workView.getView());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initalizeInject(Project project) {
        ObjectPlace objectPlace = new ObjectPlace();

        Map<Object, Object> customProperties = new HashMap<>();
        customProperties.put("project", project);
        customProperties.put("objectPlace", objectPlace);
        customProperties.put("executorCommand", executorCommand);
        Injector.setConfigurationSource(customProperties::get);
    }

    private Project initalizeProject(String name, Path path) {
        Path pathOut = Paths.get(path.toString(), "out");
        Path pathSource = Paths.get(path.toString(), "src");
        Project project = new Project(name, path, pathOut, pathSource, executorCommand);
        project.build();

        return project;
    }
}
