package ru.mrchebik.controller.actions.newProject.paths;

import ru.mrchebik.model.Project;

import java.io.File;

/**
 * Created by mrchebik on 9/2/17.
 */
public class SetProjectPaths {
    private String name;
    private String path;

    public SetProjectPaths(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public void set() {
        Project.setName(name);
        Project.setPath(path);
        Project.setPathSource(Project.getPath() + File.separator + "src");
        Project.setPathOut(Project.getPath() + File.separator + "out");
        Project.setPathOutListStructure(Project.getPathOut() + File.separator + "listJava.txt");
    }
}
