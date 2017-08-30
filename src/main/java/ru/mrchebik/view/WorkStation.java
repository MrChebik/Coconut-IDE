package ru.mrchebik.view;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

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

        primaryStage.setTitle("Coconut-IDE");

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();

        primaryStage.setWidth(primScreenBounds.getWidth() / 100 * 75);
        primaryStage.setHeight(primScreenBounds.getHeight() / 100 * 75);

        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

        primaryStage.getIcons().add(new Image(String.valueOf(NewProject.class.getResource("/img/coconut.png"))));

        primaryStage.setOnCloseRequest(event -> System.exit(0));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }
}
