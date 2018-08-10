package ru.mrchebik.call.startup;

import ru.mrchebik.gui.place.create.project.CreateProjectPlace;
import ru.mrchebik.gui.place.start.StartPlace;

public interface CallStartupWrapper {
    void callNewProject(CreateProjectPlace place);
    void callSetupHome(StartPlace startPlace);
    boolean isCorrectHome();
}
