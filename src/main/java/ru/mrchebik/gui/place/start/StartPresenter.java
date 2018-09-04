package ru.mrchebik.gui.place.start;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ru.mrchebik.call.startup.CallStartupWrapper;
import ru.mrchebik.controller.startup.StartupWrapper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.language.Language;
import ru.mrchebik.language.LanguageType;
import ru.mrchebik.language.java.call.JavaCallStartup;
import ru.mrchebik.language.java.startup.JavaStartup;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.project.Project;
import ru.mrchebik.settings.PropertyCollector;

import javax.inject.Inject;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class StartPresenter extends StartPresenterAction implements Initializable {
    @FXML
    public Button createProject, openProject, setupHomeButton;
    @FXML
    public Tooltip tooltipSetupHome;
    @FXML
    private ImageView coconutPng;
    // TODO remove
    @Inject
    private StartPlace startPlace;

    @FXML
    public BorderPane titleZone;

    public static CallStartupWrapper callStartup;
    private StartupWrapper startup;

    public static void openProject() {
        Path needed = Files.exists(Paths.get(PropertyCollector.projects)) ?
                Paths.get(PropertyCollector.projects)
                :
                Paths.get(System.getProperty("user.home"));

        var folder = needed.toFile();
        var directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(folder);
        directoryChooser.setTitle(Locale.getProperty("choose_folder_title", true));

        var selectedFile = directoryChooser.showDialog((Stage) ViewHelper.START.view.getUserData());
        if (selectedFile != null) {
            Project.isOpen = true;
            ViewHelper.START.stage.close();
            Project.initializeProject(selectedFile.getName(), selectedFile.toPath());
            ViewHelper.WORK.start();
        }
    }

    @FXML
    private void newProject() {
        callStartup.callNewProject();
    }

    @FXML
    private void openProjectA() {
        openProject();
    }

    @FXML
    private void openProjectAWithKey(KeyEvent event) {
        if (isEnter(event))
            openProject();
    }

    @FXML
    private void newProjectWithKey(KeyEvent event) {
        if (isEnter(event))
            callStartup.callNewProject();
    }

    @FXML
    private void setupHome() {
        setHomeAndEnable(createProject, callStartup, startPlace, startup);
    }

    @FXML
    private void setupHomeWithKey(KeyEvent event) {
        if (isEnter(event))
            setHomeAndEnable(createProject, callStartup, startPlace, startup);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPresenter();
        initStartup();
        initLocale();
        initNewProject(createProject, startup);
        initAnimation(coconutPng);
    }

    private void initStartup() {
        if (Language.languageType.equals(LanguageType.Java)) {
            startup = new JavaStartup();
            callStartup = new JavaCallStartup();
        }
    }

    private void initPresenter() {
        presenter = this;
    }
}
