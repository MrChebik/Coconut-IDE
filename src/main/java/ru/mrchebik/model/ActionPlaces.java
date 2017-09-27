package ru.mrchebik.model;

import lombok.AllArgsConstructor;
import ru.mrchebik.gui.place.create.file.CreateFilePlace;
import ru.mrchebik.gui.place.create.folder.CreateFolderPlace;
import ru.mrchebik.gui.place.rename.file.RenameFilePlace;
import ru.mrchebik.gui.place.rename.folder.RenameFolderPlace;

import java.nio.file.Path;

@AllArgsConstructor
public class ActionPlaces {
    private CreateFilePlace createFilePlace;
    private CreateFolderPlace createFolderPlace;
    private RenameFilePlace renameFilePlace;
    private RenameFolderPlace renameFolderPlace;

    public Path closeAndGetCreateFilePlace() {
        Path path = createFilePlace.getPath();
        createFilePlace.close();

        return path;
    }

    public Path closeAndGetCreateFolderPlace() {
        Path path = createFolderPlace.getPath();
        createFolderPlace.close();

        return path;
    }

    public Path closeAndGetRenameFilePlace() {
        Path path = renameFilePlace.getPath();
        renameFilePlace.close();

        return path;
    }

    public Path closeAndGetRenameFolderPlace() {
        Path path = renameFolderPlace.getPath();
        renameFolderPlace.close();

        return path;
    }

    public void runCreateFilePlace(Path path) {
        createFilePlace.setPath(path);
        createFilePlace.start();
    }

    public void runCreateFolderPlace(Path path) {
        createFolderPlace.setPath(path);
        createFolderPlace.start();
    }

    public void runRenameFilePlace(Path path) {
        renameFilePlace.setPath(path);
        renameFilePlace.start();
    }

    public void runRenameFolderPlace(Path path) {
        renameFolderPlace.setPath(path);
        renameFolderPlace.start();
    }
}
