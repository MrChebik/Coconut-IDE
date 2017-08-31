package ru.mrchebik.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.ScreenInfo;

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
        primaryStage.setWidth(400);
        primaryStage.setHeight(150);
        primaryStage.setResizable(false);

        primaryStage.setX((ScreenInfo.bounds.getWidth() - 400) / 2);
        primaryStage.setY((ScreenInfo.bounds.getHeight() - 150) / 2);

        primaryStage.getIcons().add(CustomIcons.logo);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void close() {
        primaryStage.close();
    }

    public static String getType() {
        return type;
    }

    public static Path getPath() {
        return path;
    }
}
