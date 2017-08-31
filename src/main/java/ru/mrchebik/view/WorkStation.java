package ru.mrchebik.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.mrchebik.controller.javafx.WorkStationController;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.Project;
import ru.mrchebik.model.Projects;
import ru.mrchebik.model.ScreenInfo;

/**
 * Created by mrchebik on 8/29/17.
 */
public class WorkStation {
    private static FXMLLoader fxmlLoader;

    public static void start() throws Exception {
        Stage primaryStage = new Stage();

        fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(StartOfWorking.class.getResource("/fxml/workStation.fxml"));

        primaryStage.setTitle(Project.getName() + " - [" + (Project.getPath().startsWith(Projects.getPath()) ? "~" + Project.getPath().substring(Projects.getPath().length() - 17) : Project.getPath()) + "] - Coconut-IDE 0.0.7");

        primaryStage.setWidth(ScreenInfo.bounds.getWidth() / 100 * 75);
        primaryStage.setHeight(ScreenInfo.bounds.getHeight() / 100 * 75);

        primaryStage.setX((ScreenInfo.bounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((ScreenInfo.bounds.getHeight() - primaryStage.getHeight()) / 2);

        primaryStage.getIcons().add(CustomIcons.logo);

        primaryStage.setOnCloseRequest(event -> System.exit(0));

        Scene scene = new Scene(fxmlLoader.load());

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
