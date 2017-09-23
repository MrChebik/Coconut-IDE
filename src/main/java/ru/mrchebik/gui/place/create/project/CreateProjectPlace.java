package ru.mrchebik.gui.place.create.project;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.screen.Screen;
import ru.mrchebik.model.screen.measurement.Point;

public class CreateProjectPlace {
    @Getter
    private Stage stage;

    public void start() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
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

        CreateProjectView createProjectView = new CreateProjectView();
        Scene scene = new Scene(createProjectView.getView());
        stage.setScene(scene);
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
