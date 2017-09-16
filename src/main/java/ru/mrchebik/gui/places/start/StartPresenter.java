package ru.mrchebik.gui.places.start;

import com.airhacks.afterburner.injection.Injector;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import ru.mrchebik.gui.places.creator.project.ProjectPlace;
import ru.mrchebik.model.Projects;

import javax.inject.Inject;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by mrchebik on 8/29/17.
 */
public class StartPresenter implements Initializable {
    @FXML private ImageView coconutPng;

    @Inject private StartPlace startPlace;

    @FXML
    private void handleNewProject() {
        Platform.runLater(() -> {
            ProjectPlace projectPlace = new ProjectPlace();
            Projects projects = new Projects();

            Map<Object, Object> customProperties = new HashMap<>();
            customProperties.put("projects", projects);
            customProperties.put("startPlace", startPlace);
            customProperties.put("projectPlace", projectPlace);
            Injector.setConfigurationSource(customProperties::get);

            projectPlace.start();
        });
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
}
