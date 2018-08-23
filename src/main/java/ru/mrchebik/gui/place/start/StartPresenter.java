package ru.mrchebik.gui.place.start;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import ru.mrchebik.call.startup.CallStartupWrapper;
import ru.mrchebik.controller.startup.StartupWrapper;
import ru.mrchebik.gui.place.create.project.CreateProjectPlace;
import ru.mrchebik.gui.place.work.WorkPlace;
import ru.mrchebik.injection.Injection;
import ru.mrchebik.language.Language;
import ru.mrchebik.language.LanguageType;
import ru.mrchebik.language.java.call.JavaCallStartup;
import ru.mrchebik.language.java.startup.JavaStartup;
import ru.mrchebik.locale.Locale;
import ru.mrchebik.project.Project;
import ru.mrchebik.project.Projects;

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
    private void openProjectA() {
        Path needed = Files.exists(Projects.path) ?
                Projects.path
                :
                Paths.get(System.getProperty("user.home"));

        var folder = needed.toFile();
        var directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(folder);
        directoryChooser.setTitle(Locale.getProperty("open_project_title", true));

        var selectedFile = directoryChooser.showDialog(startPlace.getStage());
        if (selectedFile != null) {
            Project.isOpen = true;
            startPlace.getStage().close();
            var workPlace = new WorkPlace();
            workPlace.start(selectedFile.getName(), selectedFile.toPath());
        }
    }

    @FXML
    private void openProjectAWithKey(KeyEvent event) {
        if (isEnter(event)) {

        }
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
        Injection.initInjection(startPlace, createProjectPlace);
    }

    private void initStartup() {
        if (Language.languageType.equals(LanguageType.Java)) {
            startup = new JavaStartup();
            callStartup = new JavaCallStartup();
        }
        createProjectPlace = new CreateProjectPlace();
    }

    private void initPresenter() {
        presenter = this;
    }
}
