package ru.mrchebik.controller.actions.newProject;

import ru.mrchebik.controller.actions.newProject.paths.CreateProjectPaths;
import ru.mrchebik.controller.actions.newProject.paths.SetProjectPaths;

/**
 * Created by mrchebik on 9/2/17.
 */
public class NewProject extends Thread {
    private String name;
    private String path;

    public NewProject(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public void run() {
        createProject();
    }

    private void createProject() {
        SetProjectPaths setProjectPaths = new SetProjectPaths(name, path);
        setProjectPaths.set();

        CreateProjectPaths createProjectPaths = new CreateProjectPaths();
        createProjectPaths.create();
    }
}
