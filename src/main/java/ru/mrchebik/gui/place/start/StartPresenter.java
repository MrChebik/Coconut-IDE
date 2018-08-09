package ru.mrchebik.gui.place.start;

import com.airhacks.afterburner.injection.Injector;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.gui.place.create.project.CreateProjectPlace;
import ru.mrchebik.project.Projects;
import ru.mrchebik.settings.PropertyCollector;

import javax.inject.Inject;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ResourceBundle;

public class StartPresenter implements Initializable {
    @FXML
    private ImageView coconutPng;
    @FXML
    private Button createProject;
    @Inject
    private StartPlace startPlace;

    private CreateProjectPlace createProjectPlace;

    @FXML
    private void handleNewProject() {
        initializeCreateProjectPlace();
    }

    @FXML
    private void handleNewProjectWithKey(KeyEvent event) {
        if (KeyHelper.isEnter(event))
            initializeCreateProjectPlace();
    }

    @FXML
    private void handleSetupJDK() {
        var jdkProperty = PropertyCollector.getProperty("jdk");
        var target = Paths.get(jdkProperty == null ? System.getProperty("java.home") : jdkProperty).toFile();

        var directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(target);
        directoryChooser.setTitle("Choose JDK Folder");

        var file = directoryChooser.showDialog(startPlace.getStage());
        if (file != null)
            setJDK(file.getPath());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!PropertyCollector.isJDKCorrect() && PropertyCollector.getProperty("jdk") == null)
            createProject.setDisable(true);

        var scaleTransition = new ScaleTransition(Duration.millis(2000), coconutPng);
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
        var customProperties = new HashMap<>();
        customProperties.put("createProjectPlace", createProjectPlace);
        customProperties.put("startPlace", startPlace);
        customProperties.put("projects", Projects.create());
        Injector.setConfigurationSource(customProperties::get);
    }

    private void setJDK(String pathString) {
        var pathJavac = Paths.get(pathString, "bin", PropertyCollector.getJavac());
        if (Files.exists(pathJavac)) {
            PropertyCollector.writeProperty("jdk", pathString);
            createProject.setDisable(false);
        }
    }
}
