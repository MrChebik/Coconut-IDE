package ru.mrchebik.gui.place.start;

import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import ru.mrchebik.call.startup.CallStartupWrapper;
import ru.mrchebik.controller.startup.StartupWrapper;
import ru.mrchebik.gui.key.KeyHelper;
import ru.mrchebik.gui.place.create.project.CreateProjectPlace;
import ru.mrchebik.injection.Injection;
import ru.mrchebik.language.Language;
import ru.mrchebik.language.LanguageType;
import ru.mrchebik.language.java.call.JavaCallStartup;
import ru.mrchebik.language.java.startup.JavaStartup;
import ru.mrchebik.locale.Locale;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class StartPresenter extends KeyHelper implements Initializable {
    @FXML
    private ImageView coconutPng;
    @FXML
    private Button createProject;
    @FXML
    private Tooltip tooltipSetupHome;
    @Inject
    private StartPlace startPlace;

    private CreateProjectPlace createProjectPlace;
    private CallStartupWrapper callStartup;
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
        setHomeAndEnable();
    }

    @FXML
    private void setupHomeWithKey(KeyEvent event) {
        if (isEnter(event))
            setHomeAndEnable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLocale();
        initStartup();
        initNewProject();
        Injection.initInjection(startPlace, createProjectPlace);
        initAnimation();
    }

    private void initAnimation() {
        var scaleTransition = new ScaleTransition(Duration.millis(2000), coconutPng);
        scaleTransition.setToX(1.1f);
        scaleTransition.setToY(1.1f);
        scaleTransition.setCycleCount(Timeline.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }

    private void setHomeAndEnable() {
        callStartup.callSetupHome(startup, startPlace);

        boolean isCorrect = startup.isCorrectHome();
        createProject.setDisable(isCorrect);
    }

    private void initLocale() {
        createProject.setText(Locale.NEW_PROJECT);
        tooltipSetupHome.setText(Locale.SETUP_HOME_TOOLTIP);
    }

    private void initNewProject() {
        createProjectPlace = new CreateProjectPlace();
        createProject.setDisable(startup.isCorrectHome());
    }

    private void initStartup() {
        if (Language.languageType.equals(LanguageType.Java)) {
            callStartup = new JavaCallStartup();
            startup = new JavaStartup();
        }
    }
}
