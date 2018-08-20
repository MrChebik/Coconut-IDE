package ru.mrchebik.gui.place;

import com.airhacks.afterburner.views.FXMLView;
import lombok.AllArgsConstructor;
import ru.mrchebik.gui.place.create.project.CreateProjectView;
import ru.mrchebik.gui.place.menu.create.file.CreateFileView;
import ru.mrchebik.gui.place.menu.create.folder.CreateFolderView;
import ru.mrchebik.gui.place.menu.rename.file.RenameFileView;
import ru.mrchebik.gui.place.menu.rename.folder.RenameFolderView;
import ru.mrchebik.gui.place.start.StartView;
import ru.mrchebik.gui.place.work.WorkView;

@AllArgsConstructor
public enum ViewHelper {
    PLACE_START(new StartView()),
    PLACE_CREATE_FILE(new CreateFileView()),
    PLACE_CREATE_FOLDER(new CreateFolderView()),
    PLACE_CREATE_PROJECT(new CreateProjectView()),
    PLACE_RENAME_FILE(new RenameFileView()),
    PLACE_RENAME_FOLDER(new RenameFolderView()),
    PLACE_WORK(new WorkView());

    public FXMLView view;
}
