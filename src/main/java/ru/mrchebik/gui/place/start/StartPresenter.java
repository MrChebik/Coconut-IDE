package ru.mrchebik.gui.place.start;

import com.airhacks.afterburner.injection.Injector;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import ru.mrchebik.gui.place.create.project.CreateProjectPlace;
import ru.mrchebik.model.Projects;
import ru.mrchebik.settings.PropertyCollector;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StartPresenter implements Initializable {
    @FXML
    private ImageView coconutPng;
    @Inject
    private StartPlace startPlace;

    private CreateProjectPlace createProjectPlace;

    @FXML
    private void handleNewProject() {
        initializeCreateProjectPlace();
    }

    @FXML
    private void handleSetupJDK() {
        File target = Paths.get(System.getProperty("java.home")).toFile();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(target);
        directoryChooser.setTitle("Choose JDK Folder");

        File file = directoryChooser.showDialog(startPlace.getStage());
        if (file != null) {
            setJDK(file.getPath());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(2000), coconutPng);
        scaleTransition.setToX(1.1f);
        scaleTransition.setToY(1.1f);
        scaleTransition.setCycleCount(Timeline.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }

    private void initializeCreateProjectPlace() {
        createProjectPlace = new CreateProjectPlace();
        initializeInjection();
        createProjectPlace.start();
    }

    private void initializeInjection() {
        Map<Object, Object> customProperties = new HashMap<>();
        customProperties.put("createProjectPlace", createProjectPlace);
        customProperties.put("startPlace", startPlace);
        customProperties.put("projects", Projects.create());
        Injector.setConfigurationSource(customProperties::get);
    }

    private void setJDK(String pathString) {
        Path pathJavac = Paths.get(pathString, "bin", "javac");
        if (Files.exists(pathJavac)) {
            PropertyCollector.create().writeProperty("jdk", pathString);
        }
    }
}
