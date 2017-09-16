package ru.mrchebik.gui.places.creator.object;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.screen.Screen;
import ru.mrchebik.model.screen.measurement.Point;

import java.nio.file.Path;

/**
 * Created by mrchebik on 8/30/17.
 */
public class ObjectPlace {
    private Stage primaryStage;
    private @Getter @Setter String type;
    private @Getter @Setter Path path;

    public void start() {
        primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle(type);
        primaryStage.setWidth(400);
        primaryStage.setHeight(150);
        primaryStage.setResizable(false);

        Screen screen = new Screen();
        Point point = screen.calculateCenter(400, 150);
        primaryStage.setX(point.getX());
        primaryStage.setY(point.getY());

        CustomIcons customIcons = new CustomIcons();
        primaryStage.getIcons().add(customIcons.getLogo());

        ObjectView objectView = new ObjectView();
        Scene scene = new Scene(objectView.getView());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void close() {
        primaryStage.close();
    }
}
