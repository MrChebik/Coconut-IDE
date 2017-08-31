package ru.mrchebik.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.ScreenInfo;

import java.io.IOException;

/**
 * Created by mrchebik on 8/29/17.
 */
public class NewProject {
    private static Stage primaryStage;

    public static void start() {
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(NewProject.class.getResource("/fxml/newProject.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("New Project");
        primaryStage.setWidth(500);
        primaryStage.setHeight(200);
        primaryStage.setResizable(false);

        primaryStage.setX((ScreenInfo.bounds.getWidth() - 500) / 2);
        primaryStage.setY((ScreenInfo.bounds.getHeight() - 200) / 2);

        primaryStage.getIcons().add(CustomIcons.logo);

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
