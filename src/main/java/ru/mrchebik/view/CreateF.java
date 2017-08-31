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
import java.nio.file.Path;

/**
 * Created by mrchebik on 8/30/17.
 */
public class CreateF {
    private static Stage primaryStage;
    private static String type;
    private static Path path;

    public static void start(String type, Path path) {
        CreateF.type = type;
        CreateF.path = path;

        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(CreateF.class.getResource("/fxml/createF.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle(type);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(150);
        primaryStage.setMaxHeight(150);
        primaryStage.setMaxWidth(400);

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

    public static String getType() {
        return type;
    }

    public static Path getPath() {
        return path;
    }
}
