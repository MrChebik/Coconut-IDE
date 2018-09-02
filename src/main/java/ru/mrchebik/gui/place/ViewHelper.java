package ru.mrchebik.gui.place;

import com.airhacks.afterburner.views.FXMLView;
import javafx.stage.Stage;
import ru.mrchebik.gui.place.create.project.CreateProjectView;
import ru.mrchebik.gui.place.menu.create.file.CreateFileView;
import ru.mrchebik.gui.place.menu.create.folder.CreateFolderView;
import ru.mrchebik.gui.place.menu.rename.file.RenameFileView;
import ru.mrchebik.gui.place.menu.rename.folder.RenameFolderView;
import ru.mrchebik.gui.place.menubar.about.AboutView;
import ru.mrchebik.gui.place.start.StartView;
import ru.mrchebik.gui.place.work.WorkView;
import ru.mrchebik.gui.titlebar.TitlebarView;
import ru.mrchebik.screen.measurement.Scale;

/**
 * The class contains the views, the container for the view, its title key, and its scale.
 * @see #view
 * @see #stage
 * @see #scale
 *
 * @see #start()
 */
public enum ViewHelper {
    TITLE(
            new TitlebarView(), "", Scale.EMPTY
    ),

    ABOUT(
            new AboutView(), "about_title", Scale.PLACE_MENU_ABOUT
    ),

    CREATE_FILE(
            new CreateFileView(), "create_file_title", Scale.PLACE_CREATE_FILE
    ),

    CREATE_FOLDER(
            new CreateFolderView(), "create_folder_title", Scale.PLACE_CREATE_FOLDER
    ),

    CREATE_PROJECT(
            new CreateProjectView(), "create_project_title", Scale.PLACE_CREATE_PROJECT
    ),

    RENAME_FILE(
            new RenameFileView(), "rename_file_title", Scale.PLACE_RENAME_FILE
    ),

    RENAME_FOLDER(
            new RenameFolderView(), "rename_folder_title", Scale.PLACE_RENAME_FOLDER
    ),

    START(
            new StartView(), "start_title", Scale.PLACE_START
    ),

    WORK(
            new WorkView(), "work_title", Scale.PLACE_WORK
    );

    public FXMLView view;
    public Stage stage;
    public Scale scale;
    public String key;

    ViewHelper(FXMLView view,
               String key,
               Scale scale) {
        this.view = view;
        this.scale = scale;

        this.stage = new Stage();
        this.key = key;
        //stage.setTitle(Locale.getProperty(key, true));
    }

    /**
     * Customize the window, or just show it if the window has already been created.
     */
    public void start() {
        PlaceConfig.initialize(this);
    }
}
