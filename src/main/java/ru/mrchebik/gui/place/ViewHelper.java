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

public enum ViewHelper {
    ABOUT(new AboutView()),
    CREATE_FILE(new CreateFileView()),
    CREATE_FOLDER(new CreateFolderView()),
    CREATE_PROJECT(new CreateProjectView()),
    RENAME_FILE(new RenameFileView()),
    RENAME_FOLDER(new RenameFolderView()),
    START(new StartView()),
    WORK(new WorkView());

    public FXMLView view;
    public Stage stage;

    ViewHelper(FXMLView view) {
        this.view = view;
        this.stage = new Stage();
    }
}
