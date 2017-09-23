package ru.mrchebik.gui.place.create.file;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.screen.Screen;
import ru.mrchebik.model.screen.measurement.Point;

import java.nio.file.Path;

public class CreateFilePlace {
    @Getter @Setter
    private Path path;

    private Stage stage;

    public static CreateFilePlace create() {
        return new CreateFilePlace();
    }

    public void start() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Create File");
        stage.setWidth(400);
        stage.setHeight(150);
        stage.setResizable(false);

        Screen screen = new Screen();
        Point point = screen.calculateCenter(400, 150);
        stage.setX(point.getX());
        stage.setY(point.getY());

        CustomIcons customIcons = new CustomIcons();
        stage.getIcons().add(customIcons.getLogo());

        CreateFileView createFileView = new CreateFileView();
        Scene scene = new Scene(createFileView.getView());
        stage.setScene(scene);
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
