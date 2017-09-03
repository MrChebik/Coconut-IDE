package ru.mrchebik.controller.actions.newProject.paths;

import ru.mrchebik.model.Project;

import java.io.File;
import java.io.IOException;

/**
 * Created by mrchebik on 14.05.16.
 */
public class CreateProjectPaths {
    public void create() {
        boolean isProjectCreated = createFolderToProject(Project.getPath());
        boolean isSourceCreated = createFolderToProject(Project.getPathSource());
        boolean isOutCreated = createFolderToProject(Project.getPathOut());
        boolean isListStructureCreated = createFileToProject(Project.getPathOutListStructure());
        boolean isMainCreated = createFileToProject(Project.getPathSource() + File.separator + "Main.java");

        boolean isCreatedPaths = isProjectCreated && isSourceCreated && isOutCreated && isListStructureCreated && isMainCreated;

        if (!isCreatedPaths) {
            // TODO show error window
        }
    }

    public static boolean createFolderToProject(String path) {
        File file = new File(path);

        return file.mkdir();
    }

    public static boolean createFileToProject(String path) {
        File file = new File(path);

        try {
            return file.createNewFile();
        } catch (IOException ignored) {
        }

        return false;
    }
}
