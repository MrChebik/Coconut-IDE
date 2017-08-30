package ru.mrchebik.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Created by mrchebik on 8/29/17.
 */
public class StartOfWorking extends Application {
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(StartOfWorking.class.getResource("/fxml/startOfWorking.fxml")));
        /*scene.getStylesheets().add(StartOfWorking.class.getResource("/css/start.css").toExternalForm());*/

        stage = primaryStage;
        stage.setTitle("Coconut-IDE");
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setMaxHeight(400);
        stage.setMaxWidth(600);

        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getMinWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getMinHeight()) / 2);

        stage.getIcons().add(new Image(String.valueOf(NewProject.class.getResource("/img/coconut.png"))));

        stage.setScene(scene);
        stage.show();
    }

    public static void close() {
        stage.close();
    }

    public static Stage getStage() {
        return stage;
    }
}
