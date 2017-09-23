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

    public void closeCreateFilePlace() {
        createFilePlace.close();
    }

    public void closeCreateFolderPlace() {
        createFolderPlace.close();
    }

    public void closeRenameFilePlace() {
        renameFilePlace.close();
    }

    public void closeRenameFolderPlace() {
        renameFolderPlace.close();
    }

    public Path getPathOfCreateFilePlace() {
        return createFilePlace.getPath();
    }

    public void setPathOfCreateFilePlace(Path path) {
        createFilePlace.setPath(path);
    }

    public void startCreateFilePlace() {
        createFilePlace.start();
    }

    public Path getPathOfCreateFolderPlace() {
        return createFolderPlace.getPath();
    }

    public void setPathOfCreateFolderPlace(Path path) {
        createFolderPlace.setPath(path);
    }

    public void startCreateFolderPlace() {
        createFolderPlace.start();
    }

    public Path getPathOfRenameFilePlace() {
        return renameFilePlace.getPath();
    }

    public void setPathOfRenameFilePlace(Path path) {
        renameFilePlace.setPath(path);
    }

    public void startRenameFilePlace() {
        renameFilePlace.start();
    }

    public Path getPathOfRenameFolderPlace() {
        return renameFolderPlace.getPath();
    }

    public void setPathOfRenameFolderPlace(Path path) {
        renameFolderPlace.setPath(path);
    }

    public void startRenameFolderPlace() {
        renameFolderPlace.start();
    }
}
