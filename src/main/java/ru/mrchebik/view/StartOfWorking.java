package ru.mrchebik.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.ScreenInfo;

/**
 * Created by mrchebik on 8/29/17.
 */
public class StartOfWorking extends Application {
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(StartOfWorking.class.getResource("/fxml/startOfWorking.fxml")));

        stage = primaryStage;
        stage.setTitle("Coconut-IDE");
        stage.setWidth(600);
        stage.setHeight(400);

        stage.setX((ScreenInfo.BOUNDS.getWidth() - 600) / 2);
        stage.setY((ScreenInfo.BOUNDS.getHeight() - 400) / 2);

        stage.getIcons().add(CustomIcons.logo);

        stage.setScene(scene);
        stage.show();
    }

    public static void close() {
        stage.close();
    }
}
