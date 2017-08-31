package ru.mrchebik.controller.javafx;

import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import ru.mrchebik.view.NewProject;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by mrchebik on 8/29/17.
 */
public class StartOfWorkingController implements Initializable {
    @FXML private ImageView coconutPng;

    @FXML
    private void handleNewProject() {
        Platform.runLater(NewProject::start);
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
