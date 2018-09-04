package ru.mrchebik.language.java.call;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import ru.mrchebik.call.startup.CallStartup;
import ru.mrchebik.call.startup.CallStartupWrapper;
import ru.mrchebik.controller.startup.StartupWrapper;
import ru.mrchebik.gui.place.ViewHelper;
import ru.mrchebik.gui.place.create.project.CreateProjectPresenter;
import ru.mrchebik.gui.place.start.StartPlace;
import ru.mrchebik.gui.place.start.StartPresenter;
import ru.mrchebik.language.java.settings.JavaPropertyCollector;
import ru.mrchebik.locale.Locale;

import java.io.File;

public class JavaCallStartup extends CallStartup implements CallStartupWrapper {
    @Override
    public void callNewProject() {
        ((StartPresenter) ViewHelper.START.view.getPresenter()).titleZone.setEffect(new ColorAdjust(0, 0, -0.5, 0));
        ((AnchorPane) ViewHelper.START.view.getView()).getChildren().add(ViewHelper.CREATE_PROJECT.view.getView());
        StartPresenter presenter = ((StartPresenter) ViewHelper.START.view.getPresenter());
        presenter.createProject.setFocusTraversable(false);
        presenter.openProject.setFocusTraversable(false);
        presenter.setupHomeButton.setFocusTraversable(false);
        CreateProjectPresenter projectPresenter = ((CreateProjectPresenter) ViewHelper.CREATE_PROJECT.view.getPresenter());
        projectPresenter.projectName.requestFocus();
        projectPresenter.mask.setPrefWidth(presenter.titleZone.getWidth());
        projectPresenter.mask.setPrefHeight(presenter.titleZone.getHeight());
        projectPresenter.isClickedOfNonMask = false;
    }

    @Override
    public void callSetupHome(StartupWrapper startup, StartPlace startPlace) {
        var valueJdk = JavaPropertyCollector.getProperty("jdk");
        var folderJdk = new File(valueJdk);
        var directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(folderJdk);
        directoryChooser.setTitle(Locale.getProperty("setup_home_title", false));

        var selectedFile = directoryChooser.showDialog(ViewHelper.START.stage);
        if (selectedFile != null)
            startup.setupHome(selectedFile.getPath());
    }
}
