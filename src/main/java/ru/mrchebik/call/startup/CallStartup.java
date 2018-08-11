package ru.mrchebik.call.startup;

import ru.mrchebik.controller.startup.StartupWrapper;
import ru.mrchebik.gui.place.create.project.CreateProjectPlace;
import ru.mrchebik.gui.place.start.StartPlace;

public abstract class CallStartup implements CallStartupWrapper {
    public void callNewProject(CreateProjectPlace place) {
        throw new UnsupportedOperationException();
    }

    public void callSetupHome(StartupWrapper startup, StartPlace startPlace) {
        throw new UnsupportedOperationException();
    }
}
