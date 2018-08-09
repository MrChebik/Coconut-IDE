package ru.mrchebik.gui.place.rename.folder;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.mrchebik.icons.Icons;
import ru.mrchebik.screen.Screen;
import ru.mrchebik.screen.measurement.Point;

import java.nio.file.Path;

public class RenameFolderPlace {
    @Getter @Setter
    private Path path;

    private Stage stage;

    public static RenameFolderPlace create() {
        return new RenameFolderPlace();
    }

    public void start() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Rename File");
        stage.setWidth(400);
        stage.setHeight(150);
        stage.setResizable(false);

        Screen screen = new Screen();
        Point point = screen.calculateCenter(400, 150);
        stage.setX(point.getX());
        stage.setY(point.getY());

        stage.getIcons().add(Icons.LOGO.get());

        RenameFolderView renameFolderView = new RenameFolderView();
        Scene scene = new Scene(renameFolderView.getView());
        stage.setScene(scene);
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
