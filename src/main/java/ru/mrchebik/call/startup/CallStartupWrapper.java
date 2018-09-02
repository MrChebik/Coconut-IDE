package ru.mrchebik.call.startup;

import ru.mrchebik.controller.startup.StartupWrapper;
import ru.mrchebik.gui.place.start.StartPlace;

public interface CallStartupWrapper {
    void callNewProject();

    void callSetupHome(StartupWrapper startup, StartPlace startPlace);
}
