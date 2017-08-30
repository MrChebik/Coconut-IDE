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

    public static void start(Stage primaryStage) throws Exception {
        fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(StartOfWorking.class.getResource("/fxml/workStation.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Coconut-IDE");
        primaryStage.setMinWidth(1208);
        primaryStage.setMinHeight(840);
        primaryStage.setMaxHeight(840);
        primaryStage.setMaxWidth(1208);

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getMinWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getMinHeight()) / 2);

        primaryStage.getIcons().add(new Image(String.valueOf(NewProject.class.getResource("/img/coconut.png"))));

        primaryStage.setOnCloseRequest(event -> System.exit(0));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }
}
