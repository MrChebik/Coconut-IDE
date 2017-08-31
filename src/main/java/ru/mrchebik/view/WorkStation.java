package ru.mrchebik.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.mrchebik.controller.javafx.WorkStationController;
import ru.mrchebik.model.Project;
import ru.mrchebik.model.Projects;

/**
 * Created by mrchebik on 8/29/17.
 */
public class WorkStation {
    private static FXMLLoader fxmlLoader;

    public static void start() throws Exception {
        Stage primaryStage = new Stage();

        fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(StartOfWorking.class.getResource("/fxml/workStation.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle(Project.getName() + " - [" + (Project.getPath().startsWith(Projects.getPath()) ? "~" + Project.getPath().substring(Projects.getPath().length() - 17) : Project.getPath()) + "] - Coconut-IDE 0.0.7");

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

        primaryStage.setWidth(primScreenBounds.getWidth() / 100 * 75);
        primaryStage.setHeight(primScreenBounds.getHeight() / 100 * 75);

        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

        primaryStage.getIcons().add(new Image(String.valueOf(NewProject.class.getResource("/img/coconut.png"))));

        primaryStage.setOnCloseRequest(event -> System.exit(0));

        primaryStage.setScene(scene);
        primaryStage.show();

        WorkStationController controller = fxmlLoader.getController();

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                event -> controller.loadTree())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }
}
