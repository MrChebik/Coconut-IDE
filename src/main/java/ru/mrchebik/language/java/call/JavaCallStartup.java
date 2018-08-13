package ru.mrchebik.language.java.call;

import javafx.stage.DirectoryChooser;
import ru.mrchebik.call.startup.CallStartup;
import ru.mrchebik.call.startup.CallStartupWrapper;
import ru.mrchebik.controller.startup.StartupWrapper;
import ru.mrchebik.gui.place.create.project.CreateProjectPlace;
import ru.mrchebik.gui.place.start.StartPlace;
import ru.mrchebik.language.java.settings.JavaPropertyCollector;
import ru.mrchebik.locale.Locale;

import java.io.File;

public class JavaCallStartup extends CallStartup implements CallStartupWrapper {
    @Override
    public void callNewProject(CreateProjectPlace place) {
        place.start();
    }

    @Override
    public void callSetupHome(StartupWrapper startup, StartPlace startPlace) {
        var valueJdk = JavaPropertyCollector.getProperty("jdk");
        var folderJdk = new File(valueJdk);
        var directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(folderJdk);
        directoryChooser.setTitle(Locale.SETUP_HOME_TOOLTIP);

        var selectedFile = directoryChooser.showDialog(startPlace.getStage());
        if (selectedFile != null)
            startup.setupHome(selectedFile.getPath());
    }
}
