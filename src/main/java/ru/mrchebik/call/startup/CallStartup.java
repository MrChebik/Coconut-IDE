package ru.mrchebik.call.startup;

import ru.mrchebik.controller.startup.StartupWrapper;
import ru.mrchebik.gui.place.start.StartPlace;

public abstract class CallStartup implements CallStartupWrapper {
    public void callNewProject() {
        throw new UnsupportedOperationException();
    }

    public void callSetupHome(StartupWrapper startup, StartPlace startPlace) {
        throw new UnsupportedOperationException();
    }
}
