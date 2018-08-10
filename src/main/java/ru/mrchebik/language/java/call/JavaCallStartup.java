package ru.mrchebik.language.java.call;

import javafx.stage.DirectoryChooser;
import ru.mrchebik.call.startup.CallStartup;
import ru.mrchebik.call.startup.CallStartupWrapper;
import ru.mrchebik.gui.place.create.project.CreateProjectPlace;
import ru.mrchebik.gui.place.start.StartPlace;
import ru.mrchebik.settings.PropertyCollector;

import java.nio.file.Files;
import java.nio.file.Paths;

public class JavaCallStartup extends CallStartup implements CallStartupWrapper {
    @Override
    public void callNewProject(CreateProjectPlace place) {
        place.start();
    }

    @Override
    public void callSetupHome(StartPlace startPlace) {
        var jdkProperty = PropertyCollector.getProperty("jdk");
        var target = Paths.get
                (jdkProperty == null ?
                        System.getProperty("java.home")
                        :
                        jdkProperty
                )
                .toFile();

        var directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(target);
        directoryChooser.setTitle("Select the folder of the JDK");

        var file = directoryChooser.showDialog(startPlace.getStage());
        if (file != null)
            setJDK(file.getPath());
    }

    private void setJDK(String pathString) {
        var pathJavac = Paths.get(pathString, "bin", PropertyCollector.getJavac());
        if (Files.exists(pathJavac))
            PropertyCollector.writeProperty("jdk", pathString);
    }

    public boolean isCorrectHome() {
        return !PropertyCollector.isJDKCorrect()
                && PropertyCollector.getProperty("jdk") == null;
    }
}
