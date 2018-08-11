package ru.mrchebik.language.java.call;

import javafx.stage.DirectoryChooser;
import ru.mrchebik.call.startup.CallStartup;
import ru.mrchebik.call.startup.CallStartupWrapper;
import ru.mrchebik.controller.startup.StartupWrapper;
import ru.mrchebik.gui.place.create.project.CreateProjectPlace;
import ru.mrchebik.gui.place.start.StartPlace;
import ru.mrchebik.settings.PropertyCollector;

import java.nio.file.Paths;

public class JavaCallStartup extends CallStartup implements CallStartupWrapper {
    @Override
    public void callNewProject(CreateProjectPlace place) {
        place.start();
    }

    @Override
    public void callSetupHome(StartupWrapper startup, StartPlace startPlace) {
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
            startup.setupHome(file.getPath());
    }
}
