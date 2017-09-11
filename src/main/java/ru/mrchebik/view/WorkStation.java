package ru.mrchebik.view;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.mrchebik.model.CustomIcons;
import ru.mrchebik.model.GuiceModule;
import ru.mrchebik.model.Projects;
import ru.mrchebik.model.ScreenInfo;
import ru.mrchebik.model.project.Project;

import java.io.IOException;

/**
 * Created by mrchebik on 8/29/17.
 */
public class WorkStation {
    private static FXMLLoader fxmlLoader;

    @Inject
    private static Project project;

    public static void start() throws IOException {
        Stage primaryStage = new Stage();

        Injector injector = Guice.createInjector(new GuiceModule());
        fxmlLoader = injector.getInstance(FXMLLoader.class);

        fxmlLoader.setLocation(StartOfWorking.class.getResource("/fxml/workStation.fxml"));

        primaryStage.setTitle(project.getName() + " - [" + (project.getPath().startsWith(Projects.getPath()) ? "~" + project.getPath().toString().substring(Projects.getPath().length() - 17) : project.getPath()) + "] - Coconut-IDE 0.0.9");

        primaryStage.setWidth(ScreenInfo.getBOUNDS().getWidth() / 100 * 75);
        primaryStage.setHeight(ScreenInfo.getBOUNDS().getHeight() / 100 * 75);

        primaryStage.setX((ScreenInfo.getBOUNDS().getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((ScreenInfo.getBOUNDS().getHeight() - primaryStage.getHeight()) / 2);

        primaryStage.getIcons().add(CustomIcons.getLogo());

        primaryStage.setOnCloseRequest(event -> System.exit(0));

        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }
}
