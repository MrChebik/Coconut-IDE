package ru.mrchebik.gui.place.create.folder;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import ru.mrchebik.gui.place.StageHelper;
import ru.mrchebik.icons.Icons;
import ru.mrchebik.screen.measurement.Scale;

import java.nio.file.Path;

public class CreateFolderPlace {
    @Getter @Setter
    private Path path;

    private Stage stage;

    public static CreateFolderPlace create() {
        return new CreateFolderPlace();
    }

    public void start() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Create Folder");
        stage.getIcons().add(Icons.LOGO.get());

        var scale = new Scale(400, 150);
        StageHelper.setResizableFalse(stage, scale);
        StageHelper.setPosition(stage, scale);

        CreateFolderView createFolderView = new CreateFolderView();
        Scene scene = new Scene(createFolderView.getView());
        stage.setScene(scene);
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
