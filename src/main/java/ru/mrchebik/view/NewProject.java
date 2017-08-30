package ru.mrchebik.view;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by mrchebik on 8/29/17.
 */
public class NewProject {
    private static Stage primaryStage;

    public static void start() {
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(StartOfWorking.class.getResource("/fxml/newProject.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*scene.getStylesheets().add(StartOfWorking.class.getResource("/css/start.css").toExternalForm());*/

        primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("New Project");
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(200);
        primaryStage.setMaxHeight(200);
        primaryStage.setMaxWidth(500);

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getMinWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getMinHeight()) / 2);

        primaryStage.getIcons().add(new Image(String.valueOf(NewProject.class.getResource("/img/coconut.png"))));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void close() {
        primaryStage.close();
    }

    public static Stage getStage() {
        return primaryStage;
    }
}
