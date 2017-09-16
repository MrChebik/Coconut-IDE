package ru.mrchebik.gui.places.creator.project;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.screen.Screen;
import ru.mrchebik.model.screen.measurement.Point;

/**
 * Created by mrchebik on 8/29/17.
 */
public class ProjectPlace {
    private @Getter Stage stage;

    public void start() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("New Project");
        stage.setWidth(500);
        stage.setHeight(200);
        stage.setResizable(false);

        Screen screen = new Screen();
        Point point = screen.calculateCenter(500, 200);
        stage.setX(point.getX());
        stage.setY(point.getY());

        CustomIcons customIcons = new CustomIcons();
        stage.getIcons().add(customIcons.getLogo());

        ProjectView projectView = new ProjectView();
        Scene scene = new Scene(projectView.getView());
        stage.setScene(scene);
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
