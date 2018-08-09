package ru.mrchebik.gui.place;

import com.airhacks.afterburner.views.FXMLView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mrchebik.gui.place.create.file.CreateFileView;
import ru.mrchebik.gui.place.create.folder.CreateFolderView;
import ru.mrchebik.gui.place.create.project.CreateProjectView;
import ru.mrchebik.gui.place.rename.file.RenameFileView;
import ru.mrchebik.gui.place.rename.folder.RenameFolderView;
import ru.mrchebik.gui.place.start.StartView;

@AllArgsConstructor
public enum ViewHelper {
    PLACE_START(new StartView()),
    PLACE_CREATE_FILE(new CreateFileView()),
    PLACE_CREATE_FOLDER(new CreateFolderView()),
    PLACE_CREATE_PROJECT(new CreateProjectView()),
    PLACE_RENAME_FILE(new RenameFileView()),
    PLACE_RENAME_FOLDER(new RenameFolderView());

    @Getter
    private FXMLView view;
}
