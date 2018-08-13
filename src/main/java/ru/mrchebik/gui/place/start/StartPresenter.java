package ru.mrchebik.gui.place.start;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import ru.mrchebik.call.startup.CallStartupWrapper;
import ru.mrchebik.controller.startup.StartupWrapper;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.gui.place.create.project.CreateProjectPlace;
import ru.mrchebik.injection.Injection;
import ru.mrchebik.language.Language;
import ru.mrchebik.language.LanguageType;
import ru.mrchebik.language.java.call.JavaCallStartup;
import ru.mrchebik.language.java.startup.JavaStartup;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class StartPresenter extends KeyHelper implements Initializable {
    @FXML
    Button createProject;
    @FXML
    Tooltip tooltipSetupHome;
    @FXML
    private ImageView coconutPng;
    @Inject
    private StartPlace startPlace;

    private CallStartupWrapper callStartup;
    private CreateProjectPlace createProjectPlace;
    private StartupWrapper startup;

    @FXML
    private void newProject() {
        callStartup.callNewProject(createProjectPlace);
    }

    @FXML
    private void newProjectWithKey(KeyEvent event) {
        if (isEnter(event))
            callStartup.callNewProject(createProjectPlace);
    }

    @FXML
    private void setupHome() {
        StartPresenterHelper.setHomeAndEnable(createProject, callStartup, startPlace, startup);
    }

    @FXML
    private void setupHomeWithKey(KeyEvent event) {
        if (isEnter(event))
            StartPresenterHelper.setHomeAndEnable(createProject, callStartup, startPlace, startup);
    }

    @Override
    public void initialize(URL            location,
                           ResourceBundle resources) {
                             initStartup   ();
        StartPresenterHelper.initLocale    (createProject, tooltipSetupHome);
        StartPresenterHelper.initNewProject(createProject, startup);
        StartPresenterHelper.initAnimation (coconutPng);
        Injection           .initInjection (startPlace, createProjectPlace);
    }

    private void initStartup() {
        if (Language.languageType.equals(LanguageType.Java)) {
            startup = new JavaStartup();
            callStartup = new JavaCallStartup();
        }
        createProjectPlace = new CreateProjectPlace();
    }
}
